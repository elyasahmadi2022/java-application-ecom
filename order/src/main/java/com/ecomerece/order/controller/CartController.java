package com.ecomerece.order.controller;

import com.ecomerece.order.dto.CartItemRequest;
import com.ecomerece.order.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final com.ecomerece.order.services.CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest request){

        boolean isAddToCart = cartService.addToCart(userId, request);
        if (!isAddToCart){
            return ResponseEntity.badRequest().body("Product out of stock or user not found or product not found");
        }
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Void> deleteFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId){
        boolean isDeleted = cartService.deleteFromCart(userId,productId);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/item")
    public ResponseEntity<List<CartItem>> cartItems(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok().body(cartService.cartItems(userId));
    }
}
