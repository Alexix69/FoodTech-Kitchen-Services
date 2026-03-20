package com.foodtech.kitchen.infrastructure.rest.dto;

import java.util.List;

public record OrderResponse(
        Long orderId,
        String tableNumber,
        String status,
        List<ProductRequest> products
) {
}
