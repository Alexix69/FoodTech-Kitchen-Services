package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.UserRole;

public interface StartTaskPreparationPort {
    Task execute(Long taskId, UserRole callerRole);
}
