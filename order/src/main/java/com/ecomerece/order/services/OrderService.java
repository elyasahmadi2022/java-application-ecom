package com.ecomerece.order.services;

import com.ecomerece.order.dto.OrderResponse;
import com.ecomerece.order.model.CartItem;
import com.ecomerece.order.repository.CartItemRepository;
import com.ecomerece.order.repository.OrderRepository;
import com.ecomerece.order.model.Order;
import com.ecomerece.order.model.OrderItem;
import com.ecomerece.order.dto.OrderItemDTO;
import com.ecomerece.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    public Optional<OrderResponse> saveOrder(String userId) {
        List<CartItem> cartItems = cartService.cartItems(userId);
        BigDecimal totalPrice = cartItems
                .stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                )).toList();

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService .clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getOrderItems()
                        .stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )).toList(),
                order.getCreatedAt()
        );



    }
}
