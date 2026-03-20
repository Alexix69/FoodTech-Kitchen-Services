package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.domain.model.Order;

public interface UpdateOrderPort {
    Order execute(Long orderId, String tableNumber);
}
