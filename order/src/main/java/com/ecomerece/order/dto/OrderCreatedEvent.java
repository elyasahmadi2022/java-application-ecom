package com.ecomerece.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String orderStatus;
    private String userId;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
}
