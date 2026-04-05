package com.foodtech.kitchen.infrastructure.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.infrastructure.security.JwtTokenGenerator;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.foodtech.kitchen.application.ports.out.TokenGenerator;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    @DisplayName("RED: Protected endpoint without token returns 401")
    void protectedEndpoint_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/tasks/station/BAR"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("RED: Protected endpoint with valid token returns 200")
    void protectedEndpoint_withValidToken_returns200() throws Exception {
        String cocineroToken = tokenGenerator.generateToken("cocinero-user", UserRole.COCINERO);

        mockMvc.perform(get("/api/tasks/station/HOT_KITCHEN")
            .header("Authorization", "Bearer " + cocineroToken))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("RED: Protected endpoint with expired token returns 401")
    void protectedEndpoint_withExpiredToken_returns401() throws Exception {
        Instant fixedInstant = Instant.parse("2020-01-01T00:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
        JwtTokenGenerator generator = new JwtTokenGenerator(jwtSecret, 1L, fixedClock);
        String expiredToken = generator.generateToken("auth-user", UserRole.MESERO);

        mockMvc.perform(get("/api/tasks/station/BAR")
                .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("RED: Protected endpoint with malformed token returns 401")
    void protectedEndpoint_withMalformedToken_returns401() throws Exception {
        mockMvc.perform(get("/api/tasks/station/BAR")
                .header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("RED: Protected endpoint with invalid signature returns 401")
    void protectedEndpoint_withInvalidSignature_returns401() throws Exception {
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
        JwtTokenGenerator generator = new JwtTokenGenerator(
                "different-secret-for-test-signature-1234567890",
                3600L,
                fixedClock
        );
        String invalidSignatureToken = generator.generateToken("auth-user", UserRole.MESERO);

        mockMvc.perform(get("/api/tasks/station/BAR")
                .header("Authorization", "Bearer " + invalidSignatureToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("BE5-02 (1): COCINERO JWT on POST /api/orders returns 403")
    void cocineroToken_onPostOrders_returns403() throws Exception {
        String cocineroToken = tokenGenerator.generateToken("cocinero-security", UserRole.COCINERO);
        String body = "{\"tableNumber\":\"T1\",\"products\":[{\"name\":\"Pizza\",\"type\":\"HOT_DISH\"}]}";

        mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + cocineroToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("BE5-02 (2): MESERO JWT on POST /api/orders passes Spring Security")
    void meseroToken_onPostOrders_passesSpringSecurity() throws Exception {
        String meseroToken = tokenGenerator.generateToken("mesero-security", UserRole.MESERO);
        String body = "{\"tableNumber\":\"T2\",\"products\":[{\"name\":\"Coca Cola\",\"type\":\"DRINK\"}]}";

        mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + meseroToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(401, status);
                    org.junit.jupiter.api.Assertions.assertNotEquals(403, status);
                });
    }

    @Test
    @DisplayName("BE5-02 (3): MESERO JWT on GET /api/tasks/station/BAR returns 403")
    void meseroToken_onGetTasksByStation_returns403() throws Exception {
        String meseroToken = tokenGenerator.generateToken("mesero-security-2", UserRole.MESERO);

        mockMvc.perform(get("/api/tasks/station/BAR")
                .header("Authorization", "Bearer " + meseroToken))
                .andExpect(status().isForbidden());
    }
}
