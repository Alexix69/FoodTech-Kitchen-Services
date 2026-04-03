package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class GetTasksByStationUseCaseTest {

    private GetTasksByStationUseCase useCase;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        useCase = new GetTasksByStationUseCase(taskRepository);
    }

    @Test
    @DisplayName("Should return only tasks for specified station")
    void shouldReturnOnlyTasksForSpecifiedStation() {
        Product cocaCola = new Product("Coca Cola", ProductType.DRINK);
        Product sprite = new Product("Sprite", ProductType.DRINK);

        LocalDateTime now = LocalDateTime.now();
        Task barTask1 = new Task(1L, Station.BAR, "A1", List.of(cocaCola), now);
        Task barTask2 = new Task(1L, Station.BAR, "A2", List.of(sprite), now);

        when(taskRepository.findByStationsAndStatus(Set.of(Station.BAR), TaskStatus.PENDING))
            .thenReturn(List.of(barTask1, barTask2));

        List<Task> tasks = useCase.execute(Set.of(Station.BAR), TaskStatus.PENDING);

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().allMatch(task -> task.getStation() == Station.BAR));
        verify(taskRepository, times(1)).findByStationsAndStatus(Set.of(Station.BAR), TaskStatus.PENDING);
    }

    @Test
    @DisplayName("Should return empty list when no tasks for station")
    void shouldReturnEmptyListWhenNoTasksForStation() {
        when(taskRepository.findByStationsAndStatus(Set.of(Station.BAR), TaskStatus.PENDING))
            .thenReturn(List.of());

        List<Task> tasks = useCase.execute(Set.of(Station.BAR), TaskStatus.PENDING);

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByStationsAndStatus(Set.of(Station.BAR), TaskStatus.PENDING);
    }

    @Test
    @DisplayName("COCINERO stations HOT_KITCHEN and COLD_KITCHEN return tasks from both")
    void shouldReturnTasksFromBothCocinairoStations() {
        LocalDateTime now = LocalDateTime.now();
        Product hotDish = new Product("Burger", ProductType.HOT_DISH);
        Product coldDish = new Product("Salad", ProductType.COLD_DISH);

        Task hotTask = new Task(1L, Station.HOT_KITCHEN, "A1", List.of(hotDish), now);
        Task coldTask = new Task(2L, Station.COLD_KITCHEN, "A2", List.of(coldDish), now);

        Set<Station> cocinairoStations = Set.of(Station.HOT_KITCHEN, Station.COLD_KITCHEN);
        when(taskRepository.findByStationsAndStatus(cocinairoStations, TaskStatus.PENDING))
            .thenReturn(List.of(hotTask, coldTask));

        List<Task> tasks = useCase.execute(cocinairoStations, TaskStatus.PENDING);

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getStation() == Station.HOT_KITCHEN));
        assertTrue(tasks.stream().anyMatch(t -> t.getStation() == Station.COLD_KITCHEN));
        verify(taskRepository).findByStationsAndStatus(cocinairoStations, TaskStatus.PENDING);
    }

    @Test
    @DisplayName("BARTENDER station BAR returns only BAR tasks")
    void shouldReturnOnlyBarTasksForBartender() {
        LocalDateTime now = LocalDateTime.now();
        Product drink = new Product("Beer", ProductType.DRINK);

        Task barTask = new Task(3L, Station.BAR, "A3", List.of(drink), now);

        Set<Station> bartenderStations = Set.of(Station.BAR);
        when(taskRepository.findByStationsAndStatus(bartenderStations, TaskStatus.PENDING))
            .thenReturn(List.of(barTask));

        List<Task> tasks = useCase.execute(bartenderStations, TaskStatus.PENDING);

        assertEquals(1, tasks.size());
        assertEquals(Station.BAR, tasks.get(0).getStation());
        verify(taskRepository).findByStationsAndStatus(bartenderStations, TaskStatus.PENDING);
    }
}
