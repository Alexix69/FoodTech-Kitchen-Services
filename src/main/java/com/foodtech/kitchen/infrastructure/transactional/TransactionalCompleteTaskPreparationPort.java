package com.foodtech.kitchen.infrastructure.transactional;

import com.foodtech.kitchen.application.ports.in.CompleteTaskPreparationPort;
import com.foodtech.kitchen.application.usecases.CompleteTaskPreparationUseCase;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.UserRole;
import org.springframework.transaction.annotation.Transactional;

public class TransactionalCompleteTaskPreparationPort implements CompleteTaskPreparationPort {

    private final CompleteTaskPreparationUseCase delegate;

    public TransactionalCompleteTaskPreparationPort(CompleteTaskPreparationUseCase delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional
    public Task execute(Long taskId, UserRole callerRole) {
        return delegate.execute(taskId, callerRole);
    }
}
