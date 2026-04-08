package com.foodtech.kitchen.infrastructure.rest.dto;

public record CreateOrderResponse(
    Long orderId,
    String tableNumber,
    Integer tasksCreated,
    String message
) {}