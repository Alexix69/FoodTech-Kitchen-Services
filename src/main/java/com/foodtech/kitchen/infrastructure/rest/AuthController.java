package com.foodtech.kitchen.infrastructure.rest;

import com.foodtech.kitchen.application.model.AuthResponse;
import com.foodtech.kitchen.application.ports.in.AssignUserRolePort;
import com.foodtech.kitchen.application.ports.in.AuthenticateUserPort;
import com.foodtech.kitchen.application.ports.in.RegisterUserPort;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.infrastructure.rest.dto.AssignRoleRequest;
import com.foodtech.kitchen.infrastructure.rest.dto.LoginRequest;
import com.foodtech.kitchen.infrastructure.rest.dto.LoginResponse;
import com.foodtech.kitchen.infrastructure.rest.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final RegisterUserPort registerUserPort;
    private final AuthenticateUserPort authenticateUserPort;
    private final AssignUserRolePort assignUserRolePort;

    public AuthController(RegisterUserPort registerUserPort,
                          AuthenticateUserPort authenticateUserPort,
                          AssignUserRolePort assignUserRolePort) {
        this.registerUserPort = registerUserPort;
        this.authenticateUserPort = authenticateUserPort;
        this.assignUserRolePort = assignUserRolePort;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        registerUserPort.execute(
                request.username(),
                request.email(),
                request.password(),
                UserRole.valueOf(request.role())
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authenticateUserPort.execute(request.identifier(), request.password());
        return ResponseEntity.ok(new LoginResponse(authResponse.token(), authResponse.role().name()));
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<LoginResponse> assignRole(@PathVariable Long id,
                                                     @Valid @RequestBody AssignRoleRequest request) {
        AuthResponse authResponse = assignUserRolePort.execute(id, UserRole.valueOf(request.role()));
        return ResponseEntity.ok(new LoginResponse(authResponse.token(), authResponse.role().name()));
    }
}
