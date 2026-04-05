package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.exceptions.AccessDeniedException;
import com.foodtech.kitchen.application.exceptions.InvalidTaskTransitionException;
import com.foodtech.kitchen.application.exceptions.TaskNotFoundException;
import com.foodtech.kitchen.application.ports.in.CompleteTaskPreparationPort;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.domain.services.RoleStationMapper;

public class CompleteTaskPreparationUseCase implements CompleteTaskPreparationPort {

    private final TaskRepository taskRepository;

    public CompleteTaskPreparationUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task execute(Long taskId, UserRole callerRole) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!RoleStationMapper.stationsFor(callerRole).contains(task.getStation())) {
            throw new AccessDeniedException(
                "Role " + callerRole + " cannot complete tasks at station " + task.getStation()
            );
        }

        try {
            task.complete();
        } catch (IllegalStateException e) {
            throw new InvalidTaskTransitionException(taskId, task.getStatus().name());
        }

        return taskRepository.save(task);
    }
}
