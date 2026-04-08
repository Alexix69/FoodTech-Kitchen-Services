package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.application.model.AuthResponse;
import com.foodtech.kitchen.domain.model.UserRole;

public interface AssignUserRolePort {
    AuthResponse execute(Long userId, UserRole role);
}
