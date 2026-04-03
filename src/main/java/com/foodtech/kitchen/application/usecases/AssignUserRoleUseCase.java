package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.model.AuthResponse;
import com.foodtech.kitchen.application.ports.in.AssignUserRolePort;
import com.foodtech.kitchen.application.ports.out.TokenGenerator;
import com.foodtech.kitchen.application.ports.out.UserRepository;
import com.foodtech.kitchen.domain.model.UserRole;

public class AssignUserRoleUseCase implements AssignUserRolePort {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;

    public AssignUserRoleUseCase(UserRepository userRepository, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthResponse execute(Long userId, UserRole role) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
