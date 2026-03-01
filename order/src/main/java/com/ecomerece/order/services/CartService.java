package com.ecomerece.order.services;


import com.ecomerece.order.clients.ProductServiceClient;
import com.ecomerece.order.clients.UserServiceClient;
import com.ecomerece.order.dto.CartItemRequest;
import com.ecomerece.order.dto.ProductResponse;
import com.ecomerece.order.dto.UserResponse;
import com.ecomerece.order.model.CartItem;
import com.ecomerece.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private  final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private  final UserServiceClient userServiceClient;
    private int attempts_count = 0;

//    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
    @Retry( name = "retryBreaker", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userId, CartItemRequest request){
       System.out.println("attempt_count "+ attempts_count);
       attempts_count++;
        ProductResponse productResponse = productServiceClient.getProductDetails(String.valueOf(request.getProductId()));

        if (productResponse == null){
            return false;
        }
        if(productResponse.getStockQuantity() < request.getQuantity()){
            return false;
        }
        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        if (userResponse == null){
            return false;
        }
        CartItem exsitingCartItem = cartItemRepository.findByUserIdAndProductId(userId, productResponse.getId());
        if (exsitingCartItem != null){
            exsitingCartItem.setQuantity(exsitingCartItem.getQuantity() + request.getQuantity());
            exsitingCartItem.setPrice(exsitingCartItem.getPrice().multiply(BigDecimal.valueOf(exsitingCartItem.getQuantity())));
            cartItemRepository.save(exsitingCartItem);
        }
        else{
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1002.2));
            cartItemRepository.save(cartItem);
        }
        return true;

    }
    public boolean addToCartFallBack(String userId, CartItemRequest request, Exception exception){

        System.out.println("FallBack is called here");
        return false;
    }
    public boolean deleteFromCart(String  userId, Long productId){
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> cartItems(String  userId){
        return cartItemRepository.findByUserId(userId);

    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
