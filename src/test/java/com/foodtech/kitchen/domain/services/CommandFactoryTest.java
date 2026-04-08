package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.commands.*;
import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class CommandFactoryTest {

    private CommandFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CommandFactory(List.of(
                new PrepareDrinkStrategy(),
                new PrepareHotDishStrategy(),
                new PrepareColdDishStrategy()
        ));
    }

    @Test
    @DisplayName("Debe crear PrepareDrinkCommand para productos de tipo DRINK")
    void shouldCreateDrinkCommandForDrinkProducts() {
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        List<Product> products = List.of(cocaCola);

        Command command = factory.createCommand(Station.BAR, products);

        assertInstanceOf(PrepareDrinkCommand.class, command);
    }

    @Test
    @DisplayName("Debe crear PrepareHotDishCommand para productos de tipo HOT_DISH")
    void shouldCreateHotDishCommandForHotDishProducts() {
        Product pizza = new Product("Pizza", ProductType.HOT_DISH);
        List<Product> products = List.of(pizza);

        Command command = factory.createCommand(Station.HOT_KITCHEN, products);

        assertInstanceOf(PrepareHotDishCommand.class, command);
    }

    @Test
    @DisplayName("Debe crear PrepareColdDishCommand para productos de tipo COLD_DISH")
    void shouldCreateColdDishCommandForColdDishProducts() {
        Product salad = new Product("Caesar Salad", ProductType.COLD_DISH);
        List<Product> products = List.of(salad);

        Command command = factory.createCommand(Station.COLD_KITCHEN, products);

        assertInstanceOf(PrepareColdDishCommand.class, command);
    }

    @Test
    @DisplayName("Debe lanzar excepción para estación desconocida")
    void shouldThrowExceptionForUnknownStation() {
        Product product = new Product("Test", ProductType.DRINK);
        List<Product> products = List.of(product);

        assertDoesNotThrow(() -> factory.createCommand(Station.BAR, products));
    }
}