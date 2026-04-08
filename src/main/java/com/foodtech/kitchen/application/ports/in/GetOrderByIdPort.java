package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.domain.model.Order;

public interface GetOrderByIdPort {
    Order execute(Long orderId);
}
