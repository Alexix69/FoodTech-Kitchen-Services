package com.foodtech.kitchen.application.usecases;

import com.foodtech.kitchen.application.ports.in.RegisterUserPort;
import com.foodtech.kitchen.application.ports.out.PasswordHasher;
import com.foodtech.kitchen.application.ports.out.UserRepository;
import com.foodtech.kitchen.application.exceptions.DuplicateEmailException;
import com.foodtech.kitchen.application.exceptions.DuplicateUsernameException;
import com.foodtech.kitchen.domain.model.User;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.domain.model.UserStatus;

public class RegisterUserUseCase implements RegisterUserPort {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute(String username, String email, String password, UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }
        registerUser(username, email, password, role);
    }

    public User registerWithRole(String username, String email, String rawPassword, UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }
        return registerUser(username, email, rawPassword, role);
    }

    public User execute(String username, String email, String rawPassword) {
        return registerUser(username, email, rawPassword, null);
    }

    private User registerUser(String username, String email, String rawPassword, UserRole role) {
        validateEmail(email);
        validatePassword(rawPassword);
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already registered");
        }
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username already registered");
        }
        String passwordHash = passwordHasher.hash(rawPassword);
        User user = role != null
            ? new User(username, email, passwordHash, UserStatus.ACTIVE, role)
            : new User(username, email, passwordHash, UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || email.indexOf('@', atIndex + 1) != -1) {
            throw new IllegalArgumentException("Email must contain exactly one '@'");
        }

        int dotAfterAt = email.indexOf('.', atIndex + 1);
        if (dotAfterAt == -1) {
            throw new IllegalArgumentException("Email must contain a '.' after '@'");
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (int i = 0; i < password.length(); i++) {
            char current = password.charAt(i);
            if (Character.isLetter(current)) {
                hasLetter = true;
            } else if (Character.isDigit(current)) {
                hasDigit = true;
            }
        }

        if (!hasLetter || !hasDigit) {
            throw new IllegalArgumentException("Password must contain at least one letter and one number");
        }
    }
}
