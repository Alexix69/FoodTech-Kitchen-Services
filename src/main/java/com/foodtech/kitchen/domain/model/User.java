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

    public User(String username, String email, String passwordHash, UserStatus status) {
        this(null, username, email, passwordHash, status, null, LocalDateTime.now(), null);
    }

    public User(String username, String email, String passwordHash, UserStatus status, UserRole role) {
        this(null, username, email, passwordHash, status, role, LocalDateTime.now(), null);
    }

    public User(Long id,
                String username,
                String email,
                String passwordHash,
                UserStatus status,
                LocalDateTime createdAt,
                LocalDateTime lastLoginAt) {
        this(id, username, email, passwordHash, status, null, createdAt, lastLoginAt);
    }

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
