package com.foodtech.kitchen.application.exceptions;

public class RoleAlreadyAssignedException extends RuntimeException {

    public RoleAlreadyAssignedException() {
        super("El usuario ya tiene un rol asignado");
    }
}
