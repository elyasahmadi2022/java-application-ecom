package com.ecomerece.order.services;

import com.ecomerece.order.dto.OrderCreatedEvent;
import com.ecomerece.order.dto.OrderResponse;
import com.ecomerece.order.model.CartItem;
import com.ecomerece.order.repository.CartItemRepository;
import com.ecomerece.order.repository.OrderRepository;
import com.ecomerece.order.model.Order;
import com.ecomerece.order.model.OrderItem;
import com.ecomerece.order.dto.OrderItemDTO;
import com.ecomerece.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final StreamBridge streamBridge;
//    private final RabbitTemplate rabbitTemplate;
//    @Value("${rabbitmq.exchange.name}")
//    private  String exchangeName;
//    @Value("${rabbitmq.routing.key}")
//    private  String routingKey;
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
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getOrderStatus().toString(),
                savedOrder.getUserId(),
                mapToOrderItemDTO(savedOrder.getOrderItems()),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()

        );

//        rabbitTemplate.convertAndSend(exchangeName, routingKey,
//                event
//                );
        streamBridge.send("saveOrder-out-0", event);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private List<OrderItemDTO> mapToOrderItemDTO(List<OrderItem> orderItem){
        return orderItem.stream()
                .map(dto ->
                     new OrderItemDTO(
                         dto.getId(),
                         dto.getProductId(),
                         dto.getQuantity(),
                         dto.getPrice(),
                         dto.getPrice().multiply(new BigDecimal(dto.getQuantity()))
                     )
        ).collect(Collectors.toList());
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
