package com.foodtech.kitchen.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class UserRoleTest {

    @Test
    @DisplayName("MESERO value must exist and resolve via valueOf")
    void shouldContainMeseroValue() {
        UserRole role = UserRole.valueOf("MESERO");
        assertEquals(UserRole.MESERO, role);
    }

    @Test
    @DisplayName("COCINERO value must exist and resolve via valueOf")
    void shouldContainCocineroValue() {
        UserRole role = UserRole.valueOf("COCINERO");
        assertEquals(UserRole.COCINERO, role);
    }

    @Test
    @DisplayName("BARTENDER value must exist and resolve via valueOf")
    void shouldContainBartenderValue() {
        UserRole role = UserRole.valueOf("BARTENDER");
        assertEquals(UserRole.BARTENDER, role);
    }

    @Test
    @DisplayName("Enum must have exactly three values — no fourth value exists")
    void shouldHaveExactlyThreeValues() {
        assertEquals(3, UserRole.values().length,
                "UserRole must contain exactly MESERO, COCINERO, BARTENDER");
    }

    @Test
    @DisplayName("Invalid role string must throw IllegalArgumentException")
    void shouldThrowForInvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> UserRole.valueOf("ADMIN"));
    }
}
