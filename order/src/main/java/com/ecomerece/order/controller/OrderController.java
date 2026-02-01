package com.ecomerece.order.controller;

import com.ecomerece.order.dto.OrderResponse;
import com.ecomerece.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> save(@RequestHeader("X-User-ID") String userId) {
       return orderService.saveOrder(userId)
               .map( orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.CREATED))
               .orElse(ResponseEntity.notFound().build());
    }
}
