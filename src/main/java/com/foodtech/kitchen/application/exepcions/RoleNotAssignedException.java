package com.foodtech.kitchen.application.exepcions;

/**
 * Thrown when a user attempts to authenticate but has no role assigned.
 * This occurs for users created before the RBAC feature was introduced.
 *
 * <p>Maps to HTTP 403 with body {@code { "error": "ROLE_NOT_ASSIGNED", "userId": <id> }}.
 */
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
