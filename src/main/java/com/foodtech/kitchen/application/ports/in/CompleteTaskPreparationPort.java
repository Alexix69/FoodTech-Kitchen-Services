package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.UserRole;

public interface CompleteTaskPreparationPort {
    Task execute(Long taskId, UserRole callerRole);
}
