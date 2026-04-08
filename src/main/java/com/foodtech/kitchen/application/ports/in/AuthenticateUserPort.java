package com.foodtech.kitchen.application.ports.in;

import com.foodtech.kitchen.application.model.AuthResponse;

public interface AuthenticateUserPort {
    AuthResponse execute(String emailOrUsername, String password);
}
