package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.domain.model.UserRole;

public interface RegisterUserPort {
    void execute(String username, String email, String password, UserRole role);
}
