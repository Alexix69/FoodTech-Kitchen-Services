package com.foodtech.kitchen.infrastructure.persistence.adapters;

import com.foodtech.kitchen.domain.model.*;
import com.foodtech.kitchen.infrastructure.persistence.jpa.TaskJpaRepository;
import com.foodtech.kitchen.infrastructure.persistence.jpa.entities.TaskEntity;
import com.foodtech.kitchen.infrastructure.persistence.jpa.entities.TaskProductEntity;
import com.foodtech.kitchen.infrastructure.persistence.mappers.ProductEntityMapper;
import com.foodtech.kitchen.infrastructure.persistence.mappers.TaskEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("component")
class TaskRepositoryAdapterTest {

    private TaskRepositoryAdapter adapter;
    private TaskJpaRepository jpaRepository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(TaskJpaRepository.class);
        ProductEntityMapper productMapper =
            new ProductEntityMapper();
        TaskEntityMapper mapper =
            new TaskEntityMapper(productMapper);
        adapter = new TaskRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    @DisplayName("Should save tasks using JPA repository")
    void shouldSaveTasks() {
        Product product = new Product("Coca Cola", ProductType.DRINK);
        Task task = new Task(1L, Station.BAR, "A1", List.of(product), LocalDateTime.now());

        adapter.saveAll(List.of(task));

        verify(jpaRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should find tasks by station")
    void shouldFindTasksByStation() {
        TaskProductEntity p =
            TaskProductEntity.builder()
                .name("Coca Cola").type(ProductType.DRINK).build();

        TaskEntity entity = TaskEntity.builder()
            .id(1L)
            .orderId(1L)
            .station(Station.BAR)
            .tableNumber("A1")
            .products(List.of(p))
            .createdAt(LocalDateTime.now())
            .build();
        
        when(jpaRepository.findByStation(Station.BAR))
            .thenReturn(List.of(entity));

        List<Task> tasks = adapter.findByStation(Station.BAR);

        assertEquals(1, tasks.size());
        assertEquals(Station.BAR, tasks.get(0).getStation());
        verify(jpaRepository, times(1)).findByStation(Station.BAR);
    }

    @Test
    @DisplayName("Should find all tasks")
    void shouldFindAllTasks() {
        TaskProductEntity p =
            TaskProductEntity.builder()
                .name("Coca Cola").type(ProductType.DRINK).build();

        TaskEntity entity = TaskEntity.builder()
            .id(1L)
            .orderId(1L)
            .station(Station.BAR)
            .tableNumber("A1")
            .products(List.of(p))
            .createdAt(LocalDateTime.now())
            .build();
        
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<Task> tasks = adapter.findAll();

        assertEquals(1, tasks.size());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find tasks by station and status")
    void shouldFindTasksByStationAndStatus() {
        TaskProductEntity p =
            TaskProductEntity.builder()
                .name("Coca Cola").type(ProductType.DRINK).build();

        TaskEntity completedEntity = TaskEntity.builder()
            .id(1L)
            .orderId(1L)
            .station(Station.BAR)
            .tableNumber("A1")
            .products(List.of(p))
            .status(TaskStatus.COMPLETED)
            .createdAt(LocalDateTime.now())
            .startedAt(LocalDateTime.now())
            .completedAt(LocalDateTime.now())
            .build();
        
        when(jpaRepository.findByStationAndStatus(Station.BAR, TaskStatus.COMPLETED))
            .thenReturn(List.of(completedEntity));

        List<Task> tasks = adapter.findByStationAndStatus(Station.BAR, TaskStatus.COMPLETED);

        assertEquals(1, tasks.size());
        assertEquals(Station.BAR, tasks.get(0).getStation());
        assertEquals(TaskStatus.COMPLETED, tasks.get(0).getStatus());
        verify(jpaRepository, times(1)).findByStationAndStatus(Station.BAR, TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should find PENDING tasks for HOT_KITCHEN and COLD_KITCHEN stations using FIFO order")
    void shouldFindPendingTasksForHotAndColdKitchenStations() {
        TaskProductEntity p =
            TaskProductEntity.builder()
                .name("Pasta").type(ProductType.HOT_DISH).build();

        TaskEntity hotTask = TaskEntity.builder()
            .id(1L).orderId(1L).station(Station.HOT_KITCHEN).tableNumber("B2")
            .products(List.of(p)).status(TaskStatus.PENDING).createdAt(LocalDateTime.now()).build();

        TaskEntity coldTask = TaskEntity.builder()
            .id(2L).orderId(2L).station(Station.COLD_KITCHEN).tableNumber("C3")
            .products(List.of(p)).status(TaskStatus.PENDING).createdAt(LocalDateTime.now().plusSeconds(1)).build();

        Set<Station> stations = Set.of(Station.HOT_KITCHEN, Station.COLD_KITCHEN);

        when(jpaRepository.findByStationInAndStatusOrderByCreatedAtAsc(stations, TaskStatus.PENDING))
            .thenReturn(List.of(hotTask, coldTask));

        List<Task> tasks = adapter.findByStationsAndStatus(stations, TaskStatus.PENDING);

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().allMatch(t ->
            t.getStation() == Station.HOT_KITCHEN || t.getStation() == Station.COLD_KITCHEN));
        verify(jpaRepository, times(1))
            .findByStationInAndStatusOrderByCreatedAtAsc(stations, TaskStatus.PENDING);
    }

    @Test
    @DisplayName("Should find PENDING tasks for BAR station only")
    void shouldFindPendingTasksForBarStation() {
        TaskProductEntity p =
            TaskProductEntity.builder()
                .name("Mojito").type(ProductType.DRINK).build();

        TaskEntity barTask = TaskEntity.builder()
            .id(3L).orderId(3L).station(Station.BAR).tableNumber("A1")
            .products(List.of(p)).status(TaskStatus.PENDING).createdAt(LocalDateTime.now()).build();

        Set<Station> stations = Set.of(Station.BAR);

        when(jpaRepository.findByStationInAndStatusOrderByCreatedAtAsc(stations, TaskStatus.PENDING))
            .thenReturn(List.of(barTask));

        List<Task> tasks = adapter.findByStationsAndStatus(stations, TaskStatus.PENDING);

        assertEquals(1, tasks.size());
        assertEquals(Station.BAR, tasks.get(0).getStation());
        verify(jpaRepository, times(1))
            .findByStationInAndStatusOrderByCreatedAtAsc(stations, TaskStatus.PENDING);
    }
}