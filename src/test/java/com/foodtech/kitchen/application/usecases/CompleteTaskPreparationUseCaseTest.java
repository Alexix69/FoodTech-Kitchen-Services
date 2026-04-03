package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.exceptions.AccessDeniedException;
import com.foodtech.kitchen.application.exceptions.TaskNotFoundException;
import com.foodtech.kitchen.application.ports.out.TaskRepository;
import com.foodtech.kitchen.domain.model.Product;
import com.foodtech.kitchen.domain.model.ProductType;
import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.TaskStatus;
import com.foodtech.kitchen.domain.model.UserRole;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class CompleteTaskPreparationUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CompleteTaskPreparationUseCase useCase;

    @Test
    void execute_whenCocinairoCompletesHotKitchenTask_returnsCompletedTask() {
        Long taskId = 1L;
        Product product = new Product("Burger", ProductType.HOT_DISH);
        Task task = Task.reconstruct(
            taskId, 1L, Station.HOT_KITCHEN, "A1", List.of(product),
            LocalDateTime.now(), TaskStatus.IN_PREPARATION, LocalDateTime.now(), null
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = useCase.execute(taskId, UserRole.COCINERO);

        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void execute_whenBartenderTriesHotKitchenTask_throwsAccessDeniedException() {
        Long taskId = 2L;
        Product product = new Product("Burger", ProductType.HOT_DISH);
        Task task = Task.reconstruct(
            taskId, 1L, Station.HOT_KITCHEN, "A1", List.of(product),
            LocalDateTime.now(), TaskStatus.IN_PREPARATION, LocalDateTime.now(), null
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class,
            () -> useCase.execute(taskId, UserRole.BARTENDER));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void execute_whenTaskIsInPendingState_throwsIllegalStateException() {
        Long taskId = 3L;
        Product product = new Product("Burger", ProductType.HOT_DISH);
        Task task = Task.reconstruct(
            taskId, 1L, Station.HOT_KITCHEN, "A1", List.of(product),
            LocalDateTime.now(), TaskStatus.PENDING, null, null
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(IllegalStateException.class,
            () -> useCase.execute(taskId, UserRole.COCINERO));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void execute_whenTaskNotFound_throwsTaskNotFoundException() {
        Long taskId = 99L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
            () -> useCase.execute(taskId, UserRole.COCINERO));

        verify(taskRepository, never()).save(any(Task.class));
    }
}
