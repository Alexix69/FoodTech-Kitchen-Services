package com.foodtech.kitchen.application.ports.out;

import com.foodtech.kitchen.domain.model.UserRole;

public interface TokenGenerator {
    String generateToken(String username, UserRole role);
}
