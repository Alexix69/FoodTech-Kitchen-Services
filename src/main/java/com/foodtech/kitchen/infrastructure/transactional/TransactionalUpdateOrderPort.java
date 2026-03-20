package com.foodtech.kitchen.infrastructure.transactional;

import com.foodtech.kitchen.application.ports.in.UpdateOrderPort;
import com.foodtech.kitchen.application.usecases.UpdateOrderUseCase;
import com.foodtech.kitchen.domain.model.Order;
import org.springframework.transaction.annotation.Transactional;

public class TransactionalUpdateOrderPort implements UpdateOrderPort {

    private final UpdateOrderUseCase delegate;

    public TransactionalUpdateOrderPort(UpdateOrderUseCase delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional
    public Order execute(Long orderId, String tableNumber) {
        return delegate.execute(orderId, tableNumber);
    }
}
