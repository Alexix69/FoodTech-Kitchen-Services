package com.foodtech.kitchen.application.model;

import com.foodtech.kitchen.domain.model.UserRole;

public record AuthResponse(String token, UserRole role) {}
