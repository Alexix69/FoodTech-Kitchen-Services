package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.Product;
import com.foodtech.kitchen.domain.model.ProductType;
import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class TaskFactoryTest {

    private TaskFactory taskFactory;

    @BeforeEach
    void setUp() {
        taskFactory = new TaskFactory();
    }

    @Test
    @DisplayName("Should create tasks for multiple stations from product map")
    void shouldCreateTasksForMultipleStations() {
        Product drink = new Product("Coca Cola", ProductType.DRINK);
        Product hotDish = new Product("Pizza", ProductType.HOT_DISH);
        Map<Station, List<Product>> productsByStation = Map.of(
            Station.BAR, List.of(drink),
            Station.HOT_KITCHEN, List.of(hotDish)
        );

        List<Task> tasks = taskFactory.createTasks(1L, "A1", productsByStation);

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getStation() == Station.BAR));
        assertTrue(tasks.stream().anyMatch(t -> t.getStation() == Station.HOT_KITCHEN));
    }

    @Test
    @DisplayName("Should assign HOT_KITCHEN station for hot dish product")
    void shouldAssignHotKitchenStationForHotDish() {
        Product hotDish = new Product("Pasta", ProductType.HOT_DISH);
        Map<Station, List<Product>> productsByStation = Map.of(Station.HOT_KITCHEN, List.of(hotDish));

        List<Task> tasks = taskFactory.createTasks(1L, "B2", productsByStation);

        assertEquals(1, tasks.size());
        assertEquals(Station.HOT_KITCHEN, tasks.get(0).getStation());
    }

    @Test
    @DisplayName("Should assign BAR station for drink product")
    void shouldAssignBarStationForDrink() {
        Product drink = new Product("Mojito", ProductType.DRINK);
        Map<Station, List<Product>> productsByStation = Map.of(Station.BAR, List.of(drink));

        List<Task> tasks = taskFactory.createTasks(1L, "A1", productsByStation);

        assertEquals(1, tasks.size());
        assertEquals(Station.BAR, tasks.get(0).getStation());
    }

    @Test
    @DisplayName("Should return empty list when productsByStation map is empty")
    void shouldReturnEmptyListWhenNoProducts() {
        List<Task> tasks = taskFactory.createTasks(1L, "A1", Map.of());

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Should throw when orderId is null")
    void shouldThrowWhenOrderIdIsNull() {
        Product drink = new Product("Mojito", ProductType.DRINK);
        Map<Station, List<Product>> productsByStation = Map.of(Station.BAR, List.of(drink));

        assertThrows(IllegalArgumentException.class,
            () -> taskFactory.createTasks(null, "A1", productsByStation));
    }
}
