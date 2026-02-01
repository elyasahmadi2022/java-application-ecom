package com.ecomerece.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
