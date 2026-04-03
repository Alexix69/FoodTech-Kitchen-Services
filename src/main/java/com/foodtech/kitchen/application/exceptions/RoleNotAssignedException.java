package com.foodtech.kitchen.application.exceptions;

public class RoleNotAssignedException extends RuntimeException {

    private final Long userId;

    public RoleNotAssignedException(Long userId) {
        super("El usuario con id " + userId + " no tiene un rol asignado");
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
