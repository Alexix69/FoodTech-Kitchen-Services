package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.model.AuthResponse;
import com.foodtech.kitchen.application.ports.out.PasswordHasher;
import com.foodtech.kitchen.application.ports.out.TokenGenerator;
import com.foodtech.kitchen.application.ports.out.UserRepository;
import com.foodtech.kitchen.domain.model.User;
import com.foodtech.kitchen.domain.model.UserStatus;

public class AuthenticateUserUseCase {
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordHasher passwordHasher;

    public AuthenticateUserUseCase(UserRepository userRepository,
                                   TokenGenerator tokenGenerator,
                                   PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordHasher = passwordHasher;
    }

    public AuthResponse execute(String identifier, String password) {
        User user = userRepository.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordHasher.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User is not active");
        }
        String token = tokenGenerator.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getRole());
    }
}
