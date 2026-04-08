package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.exceptions.RoleAlreadyAssignedException;
import com.foodtech.kitchen.application.exceptions.UserNotFoundException;
import com.foodtech.kitchen.application.model.AuthResponse;
import com.foodtech.kitchen.application.ports.out.TokenGenerator;
import com.foodtech.kitchen.application.ports.out.UserRepository;
import com.foodtech.kitchen.domain.model.User;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.domain.model.UserStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AssignUserRoleUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private AssignUserRoleUseCase useCase;

    @Test
    void execute_whenUserHasNoRole_assignsRoleAndReturnsAuthResponse() {
        Long userId = 1L;
        User userWithoutRole = new User(
            userId, "jdoe", "jdoe@example.com", "hashedPwd",
            UserStatus.ACTIVE, LocalDateTime.now(), null
        );
        User savedUser = new User(
            userId, "jdoe", "jdoe@example.com", "hashedPwd",
            UserStatus.ACTIVE, UserRole.COCINERO, LocalDateTime.now(), null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithoutRole));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenGenerator.generateToken("jdoe", UserRole.COCINERO)).thenReturn("jwt-token");

        AuthResponse result = useCase.execute(userId, UserRole.COCINERO);

        assertEquals("jwt-token", result.token());
        assertEquals(UserRole.COCINERO, result.role());
    }

    @Test
    void execute_whenUserAlreadyHasRole_throwsRoleAlreadyAssignedException() {
        Long userId = 2L;
        User userWithRole = new User(
            userId, "jdoe2", "jdoe2@example.com", "hashedPwd",
            UserStatus.ACTIVE, UserRole.MESERO, LocalDateTime.now(), null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithRole));

        assertThrows(RoleAlreadyAssignedException.class,
            () -> useCase.execute(userId, UserRole.COCINERO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void execute_whenUserNotFound_throwsUserNotFoundException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
            () -> useCase.execute(userId, UserRole.BARTENDER));
    }
}
