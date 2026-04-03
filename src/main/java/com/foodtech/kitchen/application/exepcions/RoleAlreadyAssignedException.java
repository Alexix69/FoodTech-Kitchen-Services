package com.foodtech.kitchen.application.exepcions;

/**
 * Thrown when a caller attempts to assign a role to a user who already has one.
 * The role field is immutable after first assignment; this exception enforces that invariant.
 *
 * <p>Maps to HTTP 409 Conflict.
 */
public class RoleAlreadyAssignedException extends RuntimeException {

    public RoleAlreadyAssignedException() {
        super("El usuario ya tiene un rol asignado");
    }
}
