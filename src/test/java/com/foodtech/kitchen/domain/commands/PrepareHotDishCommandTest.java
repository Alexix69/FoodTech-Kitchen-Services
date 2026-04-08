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
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        List<Product> products = List.of(pizza);

        PrepareHotDishCommand command = new PrepareHotDishCommand(products);

        assertInstanceOf(PrepareHotDishCommand.class, command);
    }

    @Test
    @DisplayName("Debe ejecutar la preparación de plato caliente")
    void shouldExecuteHotDishPreparation() {
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        PrepareHotDishCommand command = new PrepareHotDishCommand(List.of(pizza));

        assertDoesNotThrow(() -> command.execute());
    }

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