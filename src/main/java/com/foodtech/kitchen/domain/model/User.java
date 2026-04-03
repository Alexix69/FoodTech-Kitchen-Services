package com.foodtech.kitchen.domain.model;

import java.time.LocalDateTime;

public class User {
    private final Long id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final UserStatus status;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLoginAt;

    /** Convenience constructor — no role assigned (pre-RBAC users). */
    public User(String username, String email, String passwordHash, UserStatus status) {
        this(null, username, email, passwordHash, status, null, LocalDateTime.now(), null);
    }

    /** Convenience constructor — role assigned at registration time. */
    public User(String username, String email, String passwordHash, UserStatus status, UserRole role) {
        this(null, username, email, passwordHash, status, role, LocalDateTime.now(), null);
    }

    /** Full-arg reconstruction constructor used by persistence adapters. */
    public User(Long id,
                String username,
                String email,
                String passwordHash,
                UserStatus status,
                LocalDateTime createdAt,
                LocalDateTime lastLoginAt) {
        this(id, username, email, passwordHash, status, null, createdAt, lastLoginAt);
    }

    /** Full-arg reconstruction constructor including role — used by persistence adapters. */
    public User(Long id,
                String username,
                String email,
                String passwordHash,
                UserStatus status,
                UserRole role,
                LocalDateTime createdAt,
                LocalDateTime lastLoginAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserRole getRole() {
        return role;
    }

    /**
     * Returns {@code true} if this user has been assigned a role.
     * Pre-existing users created before the RBAC feature was introduced may
     * have a {@code null} role; this method provides a safe null-check.
     */
    public boolean hasRole() {
        return role != null;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }
}
