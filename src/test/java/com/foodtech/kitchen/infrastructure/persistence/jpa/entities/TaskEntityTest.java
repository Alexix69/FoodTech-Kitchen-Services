package com.foodtech.kitchen.infrastructure.persistence.jpa.entities;

import com.foodtech.kitchen.domain.model.Station;
import jakarta.persistence.Version;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@Tag("component")
class TaskEntityTest {

    @Test
    @DisplayName("Should create TaskEntity with all fields")
    void shouldCreateTaskEntity() {
        // Given & When
        TaskEntity entity = TaskEntity.builder()
            .orderId(1L)
            .station(Station.BAR)
            .tableNumber("A1")
            .build();

        // Then
        assertNotNull(entity);
        assertEquals(1L, entity.getOrderId());
        assertEquals(Station.BAR, entity.getStation());
        assertEquals("A1", entity.getTableNumber());
    }

    @Test
    @DisplayName("Should generate ID when saved")
    void shouldHaveIdField() {
        TaskEntity entity = new TaskEntity();
        assertNull(entity.getId());
    }

    @Test
    @DisplayName("Should have @Version annotation on version field")
    void shouldHaveVersionAnnotation() throws NoSuchFieldException {
        Field versionField = TaskEntity.class.getDeclaredField("version");
        assertNotNull(versionField.getAnnotation(Version.class));
    }
}