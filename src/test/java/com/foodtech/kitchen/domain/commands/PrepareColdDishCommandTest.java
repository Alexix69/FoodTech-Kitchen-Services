package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class PrepareColdDishCommandTest {

    @Test
    @DisplayName("Debe crear comando de plato frío con estación correcta")
    void shouldCreateColdDishCommandWithCorrectStation() {
        // Given
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        List<Product> products = List.of(salad);

        // When
        PrepareColdDishCommand command = new PrepareColdDishCommand(products);

        // Then
        assertInstanceOf(PrepareColdDishCommand.class, command);
    }

    @Test
    @DisplayName("Debe ejecutar la preparación de plato frío")
    void shouldExecuteColdDishPreparation() {
        // Given
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        PrepareColdDishCommand command = new PrepareColdDishCommand(List.of(salad));

        // When & Then
        assertDoesNotThrow(() -> command.execute());
    }

    /**
     * BE1-06 / FR-029: simulation removed — execute() must not sleep.
     * With Thread.sleep(5s per dish), one dish would take >= 5000ms.
     * After removing the simulation, execution must complete in < 500ms.
     */
    @Test
    @DisplayName("FR-029: execute() must complete without Thread.sleep simulation (< 500ms)")
    void execute_completesWithoutSimulation_noThreadSleep() {
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        PrepareColdDishCommand command = new PrepareColdDishCommand(List.of(salad));

        long start = System.currentTimeMillis();
        command.execute();
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed < 500,
                "execute() slept for " + elapsed + "ms — Thread.sleep simulation must be removed (FR-029)");
    }
}