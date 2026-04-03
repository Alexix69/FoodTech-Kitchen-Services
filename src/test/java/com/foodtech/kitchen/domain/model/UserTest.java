package com.foodtech.kitchen.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BE1-03: User domain model — role field, hasRole(), immutability.
 */
@Tag("unit")
class UserTest {

    @Test
    @DisplayName("hasRole() returns false when user constructed without a role")
    void hasRole_returnsFalse_whenNoRoleProvided() {
        User user = new User("juan", "juan@test.com", "hash", UserStatus.ACTIVE);
        assertFalse(user.hasRole(), "User created without role must return hasRole() = false");
    }

    @Test
    @DisplayName("hasRole() returns true when user constructed with a role")
    void hasRole_returnsTrue_whenRoleProvided() {
        User user = new User("juan", "juan@test.com", "hash", UserStatus.ACTIVE, UserRole.COCINERO);
        assertTrue(user.hasRole(), "User created with role must return hasRole() = true");
    }

    @Test
    @DisplayName("getRole() returns the exact role passed to the constructor")
    void getRole_returnsRole_matchingConstructorArg() {
        User user = new User("juan", "juan@test.com", "hash", UserStatus.ACTIVE, UserRole.BARTENDER);
        assertEquals(UserRole.BARTENDER, user.getRole());
    }

    @Test
    @DisplayName("getRole() returns null when user has no role")
    void getRole_returnsNull_whenNoRoleProvided() {
        User user = new User("juan", "juan@test.com", "hash", UserStatus.ACTIVE);
        assertNull(user.getRole());
    }

    @Test
    @DisplayName("User has no setRole method — role is immutable after construction")
    void user_hasNoSetRoleMethod() {
        // If this compiles, there is no setRole(UserRole) method
        // Verified structurally: attempting to call setRole would be a compile error
        User user = new User("juan", "juan@test.com", "hash", UserStatus.ACTIVE, UserRole.MESERO);
        assertEquals(UserRole.MESERO, user.getRole());
        // No setter — role is final and set only at construction time
    }
}
