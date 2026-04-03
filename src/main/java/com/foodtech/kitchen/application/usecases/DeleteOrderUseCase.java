package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.exceptions.OrderNotFoundException;
import com.foodtech.kitchen.application.ports.in.DeleteOrderPort;
import com.foodtech.kitchen.application.ports.out.OrderRepository;

public class DeleteOrderUseCase implements DeleteOrderPort {

    private final OrderRepository orderRepository;

    public DeleteOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(Long orderId) {
        boolean exists = orderRepository.findById(orderId).isPresent();
        if (!exists) {
            throw new OrderNotFoundException(orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
