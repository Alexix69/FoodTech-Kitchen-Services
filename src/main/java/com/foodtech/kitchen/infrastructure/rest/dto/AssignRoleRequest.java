package com.foodtech.kitchen.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequest(
    @NotBlank String role
) {}
