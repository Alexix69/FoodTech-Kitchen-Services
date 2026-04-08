package com.foodtech.kitchen.infrastructure.rest.dto;

import java.util.List;

public record CreateOrderRequest(
    String tableNumber,
    List<ProductRequest> products
) {}