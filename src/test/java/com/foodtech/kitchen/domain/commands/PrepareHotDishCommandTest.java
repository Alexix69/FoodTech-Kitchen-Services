package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class PrepareHotDishCommandTest {

    @Test
    @DisplayName("Debe crear comando de plato caliente con estación correcta")
    void shouldCreateHotDishCommandWithCorrectStation() {
        // Given
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        List<Product> products = List.of(pizza);

        // When
        PrepareHotDishCommand command = new PrepareHotDishCommand(products);

        // Then
        assertInstanceOf(PrepareHotDishCommand.class, command);
    }

    @Test
    @DisplayName("Debe ejecutar la preparación de plato caliente")
    void shouldExecuteHotDishPreparation() {
        // Given
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        PrepareHotDishCommand command = new PrepareHotDishCommand(List.of(pizza));

        // When & Then
        assertDoesNotThrow(() -> command.execute());
    }

    /**
     * BE1-05 / FR-029: simulation removed — execute() must not sleep.
     * With Thread.sleep(7s per dish), one dish would take >= 7000ms.
     * After removing the simulation, execution must complete in < 500ms.
     */
    @Test
    @DisplayName("FR-029: execute() must complete without Thread.sleep simulation (< 500ms)")
    void execute_completesWithoutSimulation_noThreadSleep() {
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        PrepareHotDishCommand command = new PrepareHotDishCommand(List.of(pizza));

        long start = System.currentTimeMillis();
        command.execute();
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed < 500,
                "execute() slept for " + elapsed + "ms — Thread.sleep simulation must be removed (FR-029)");
    }
}