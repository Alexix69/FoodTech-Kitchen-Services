package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.exceptions.RoleAlreadyAssignedException;
import com.foodtech.kitchen.application.exceptions.UserNotFoundException;
import com.foodtech.kitchen.application.model.AuthResponse;
import com.foodtech.kitchen.application.ports.in.AssignUserRolePort;
import com.foodtech.kitchen.application.ports.out.TokenGenerator;
import com.foodtech.kitchen.application.ports.out.UserRepository;
import com.foodtech.kitchen.domain.model.User;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.domain.model.UserStatus;

import java.time.LocalDateTime;

public class AssignUserRoleUseCase implements AssignUserRolePort {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;

    public AssignUserRoleUseCase(UserRepository userRepository, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthResponse execute(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.hasRole()) {
            throw new RoleAlreadyAssignedException();
        }

        User updatedUser = new User(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPasswordHash(),
            UserStatus.ACTIVE,
            role,
            LocalDateTime.now(),
            user.getLastLoginAt()
        );

        User savedUser = userRepository.save(updatedUser);
        String token = tokenGenerator.generateToken(savedUser.getUsername(), role);
        return new AuthResponse(token, role);
    }
}
