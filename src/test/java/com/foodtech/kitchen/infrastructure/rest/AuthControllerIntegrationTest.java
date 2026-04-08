package com.foodtech.kitchen.infrastructure.rest;

import com.foodtech.kitchen.application.ports.out.PasswordHasher;
import com.foodtech.kitchen.application.ports.out.UserRepository;
import com.foodtech.kitchen.domain.model.User;
import com.foodtech.kitchen.domain.model.UserStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    @DisplayName("RED: POST /api/auth/register should return 201")
    void registerShouldReturn201() throws Exception {
        String requestBody = "{\"username\":\"new-user\",\"email\":\"new-user@example.com\",\"password\":\"abc123\",\"role\":\"COCINERO\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("RED: POST /api/auth/login should return 200 with token")
    void loginShouldReturnToken() throws Exception {
        String requestBody = "{\"identifier\":\"jdoe@example.com\",\"password\":\"abc123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(isEmptyString())));
    }

    @Test
    @DisplayName("RED: register with invalid email returns 400")
    void register_withInvalidEmail_returns400() throws Exception {
        String requestBody = "{\"username\":\"user-invalid-email\",\"email\":\"not-an-email\",\"password\":\"abc123\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("RED: register with weak password returns 400")
    void register_withWeakPassword_returns400() throws Exception {
        String requestBody = "{\"username\":\"user-weak-pass\",\"email\":\"weak-pass@example.com\",\"password\":\"abc\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("RED: register with duplicate email returns 409")
    void register_duplicateEmail_returns409() throws Exception {
        String firstBody = "{\"username\":\"user-dup-email-1\",\"email\":\"dup-email@example.com\",\"password\":\"abc123\",\"role\":\"MESERO\"}";
        String secondBody = "{\"username\":\"user-dup-email-2\",\"email\":\"dup-email@example.com\",\"password\":\"abc123\",\"role\":\"MESERO\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondBody))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("RED: register with duplicate username returns 409")
    void register_duplicateUsername_returns409() throws Exception {
        String firstBody = "{\"username\":\"dup-username\",\"email\":\"dup-username-1@example.com\",\"password\":\"abc123\",\"role\":\"BARTENDER\"}";
        String secondBody = "{\"username\":\"dup-username\",\"email\":\"dup-username-2@example.com\",\"password\":\"abc123\",\"role\":\"BARTENDER\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondBody))
                .andExpect(status().isConflict());
    }

        @Test
        @DisplayName("RED: register with null username returns 400")
        void register_nullUsername_returns400() throws Exception {
                String requestBody = "{\"username\":null,\"email\":\"valid-null-user@example.com\",\"password\":\"abc123\"}";

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("RED: register with null email returns 400")
        void register_nullEmail_returns400() throws Exception {
                String requestBody = "{\"username\":\"valid-null-email\",\"email\":null,\"password\":\"abc123\"}";

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("RED: register with null password returns 400")
        void register_nullPassword_returns400() throws Exception {
                String requestBody = "{\"username\":\"valid-null-pass\",\"email\":\"valid-null-pass@example.com\",\"password\":null}";

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("RED: register with empty body returns 400")
        void register_emptyBody_returns400() throws Exception {
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(""))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("RED: register with malformed JSON returns 400")
        void register_malformedJson_returns400() throws Exception {
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ invalid json }"))
                                .andExpect(status().isBadRequest());
        }

    @Test
    @DisplayName("BE5-01 (2): register without role returns 400 with role message")
    void register_withoutRole_returns400WithRoleMessage() throws Exception {
        String requestBody = "{\"username\":\"norole-user\",\"email\":\"norole@example.com\",\"password\":\"abc123\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Debe seleccionar un rol para continuar"));
    }

    @Test
    @DisplayName("BE5-01 (3): register with invalid role string returns 400")
    void register_withInvalidRoleString_returns400() throws Exception {
        String requestBody = "{\"username\":\"badrole-user\",\"email\":\"badrole@example.com\",\"password\":\"abc123\",\"role\":\"DIRECTOR\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("BE5-01 (4): login response contains role field")
    void login_responseContainsRoleField() throws Exception {
        String requestBody = "{\"identifier\":\"jdoe@example.com\",\"password\":\"abc123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(isEmptyString())))
                .andExpect(jsonPath("$.role").value("MESERO"));
    }

    @Test
    @DisplayName("BE5-01 (5): login with null-role user returns 403 ROLE_NOT_ASSIGNED")
    void login_withNullRoleUser_returns403WithRoleNotAssigned() throws Exception {
        User noRoleUser = new User("norole-login", "norole-login@example.com",
                passwordHasher.hash("abc123"), UserStatus.ACTIVE);
        User saved = userRepository.save(noRoleUser);

        String requestBody = "{\"identifier\":\"norole-login@example.com\",\"password\":\"abc123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("ROLE_NOT_ASSIGNED"))
                .andExpect(jsonPath("$.userId").value(saved.getId()));
    }

    @Test
    @DisplayName("BE5-01 (6): PATCH /api/users/{id}/role happy path returns 200 with token and role")
    void assignRole_happyPath_returns200WithTokenAndRole() throws Exception {
        User noRoleUser = new User("assign-role-user", "assign-role@example.com",
                passwordHasher.hash("abc123"), UserStatus.ACTIVE);
        User saved = userRepository.save(noRoleUser);

        String requestBody = "{\"role\":\"COCINERO\"}";

        mockMvc.perform(patch("/api/users/" + saved.getId() + "/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(isEmptyString())))
                .andExpect(jsonPath("$.role").value("COCINERO"));
    }

    @Test
    @DisplayName("BE5-01 (7): PATCH /api/users/{id}/role when role already set returns 409")
    void assignRole_whenRoleAlreadySet_returns409() throws Exception {
        Long jdoeId = userRepository.findByEmailOrUsername("jdoe@example.com").get().getId();

        String requestBody = "{\"role\":\"COCINERO\"}";

        mockMvc.perform(patch("/api/users/" + jdoeId + "/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());
    }
}
