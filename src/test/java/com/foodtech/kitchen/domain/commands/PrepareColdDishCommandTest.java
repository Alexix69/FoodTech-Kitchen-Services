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
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        List<Product> products = List.of(salad);

        PrepareColdDishCommand command = new PrepareColdDishCommand(products);

        assertInstanceOf(PrepareColdDishCommand.class, command);
    }

    @Test
    @DisplayName("Debe ejecutar la preparación de plato frío")
    void shouldExecuteColdDishPreparation() {
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        PrepareColdDishCommand command = new PrepareColdDishCommand(List.of(salad));

        assertDoesNotThrow(() -> command.execute());
    }

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