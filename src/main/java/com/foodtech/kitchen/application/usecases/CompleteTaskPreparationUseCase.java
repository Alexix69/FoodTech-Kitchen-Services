package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.in.CompleteTaskPreparationPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.UserRole;

public class CompleteTaskPreparationUseCase implements CompleteTaskPreparationPort {

    private final TaskRepository taskRepository;

    public CompleteTaskPreparationUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task execute(Long taskId, UserRole callerRole) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
