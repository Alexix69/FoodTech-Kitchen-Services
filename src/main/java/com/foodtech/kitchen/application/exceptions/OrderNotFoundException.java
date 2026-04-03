package com.foodtech.kitchen.application.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Order not found with id: " + orderId);

    }

}
