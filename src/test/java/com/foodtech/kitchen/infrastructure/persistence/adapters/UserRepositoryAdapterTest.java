package com.foodtech.kitchen.infrastructure.persistence.adapters;

import com.foodtech.kitchen.domain.model.User;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.domain.model.UserStatus;
import com.foodtech.kitchen.infrastructure.persistence.jpa.UserJpaRepository;
import com.foodtech.kitchen.infrastructure.persistence.jpa.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("component")
class UserRepositoryAdapterTest {

    private UserRepositoryAdapter adapter;
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        userJpaRepository = mock(UserJpaRepository.class);
        adapter = new UserRepositoryAdapter(userJpaRepository);
    }

    @Test
    @DisplayName("findById with known ID returns mapped domain User")
    void findById_withKnownId_returnsMappedUser() {
        UserEntity entity = new UserEntity();
        entity.setId(42L);
        entity.setUsername("alice");
        entity.setEmail("alice@example.com");
        entity.setPasswordHash("hash");
        entity.setStatus(UserStatus.ACTIVE.name());
        entity.setRole(UserRole.COCINERO);
        entity.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        when(userJpaRepository.findById(42L)).thenReturn(Optional.of(entity));

        Optional<User> result = adapter.findById(42L);

        assertTrue(result.isPresent());
        User user = result.get();
        assertEquals(42L, user.getId());
        assertEquals("alice", user.getUsername());
        assertEquals(UserRole.COCINERO, user.getRole());
        verify(userJpaRepository, times(1)).findById(42L);
    }

    @Test
    @DisplayName("findById with unknown ID returns Optional.empty()")
    void findById_withUnknownId_returnsEmpty() {
        when(userJpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = adapter.findById(999L);

        assertFalse(result.isPresent());
        verify(userJpaRepository, times(1)).findById(999L);
    }
}
