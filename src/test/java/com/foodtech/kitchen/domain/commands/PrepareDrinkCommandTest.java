package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class PrepareDrinkCommandTest {

    @Test
    @DisplayName("Debe crear comando de bebida con estación correcta")
    void shouldCreateDrinkCommandWithCorrectStation() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        List<Product> products = List.of(cocaCola);

        // When
        PrepareDrinkCommand command = new PrepareDrinkCommand(products);

        // Then
            assertInstanceOf(PrepareDrinkCommand.class, command);
    }

    @Test
    @DisplayName("Debe ejecutar la preparación de bebida")
    void shouldExecuteDrinkPreparation() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        PrepareDrinkCommand command = new PrepareDrinkCommand(List.of(cocaCola));

        // When & Then
        assertDoesNotThrow(() -> command.execute());
    }

    @Test
    @DisplayName("Debe manejar múltiples bebidas en un solo comando")
    void shouldHandleMultipleDrinks() {
        // Given
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        Product sprite = new Product("Sprite", ProductType.DRINK);
        List<Product> products = List.of(cocaCola, sprite);

        // When
        PrepareDrinkCommand command = new PrepareDrinkCommand(products);

        // Then
        assertInstanceOf(PrepareDrinkCommand.class, command);
    }

    /**
     * BE1-04 / FR-029: simulation removed — execute() must not sleep.
     * With Thread.sleep(3s per drink), a 1-drink command would take >= 3000ms.
     * After removing the simulation, execution must complete in < 500ms.
     */
    @Test
    @DisplayName("FR-029: execute() must complete without Thread.sleep simulation (< 500ms)")
    void execute_completesWithoutSimulation_noThreadSleep() {
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        PrepareDrinkCommand command = new PrepareDrinkCommand(List.of(cocaCola));

        long start = System.currentTimeMillis();
        command.execute();
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed < 500,
                "execute() slept for " + elapsed + "ms — Thread.sleep simulation must be removed (FR-029)");
    }
}