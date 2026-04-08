package com.foodtech.kitchen.infrastructure.security;

import com.foodtech.kitchen.domain.model.UserRole;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("component")
class JwtAuthenticationFilterTest {

    private static final String SECRET = "test-secret-should-be-long-enough-for-hs256-123456";
    private static final long EXPIRATION_SECONDS = 3600L;

    private JwtTokenGenerator tokenGenerator;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        Instant now = Instant.parse("2025-01-01T00:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);
        tokenGenerator = new JwtTokenGenerator(SECRET, EXPIRATION_SECONDS, clock);
        JwtTokenValidator tokenValidator = new JwtTokenValidator(SECRET, clock);
        filter = new JwtAuthenticationFilter(tokenValidator);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withMeseroRoleToken_setsRoleMeseroAuthority() throws Exception {
        String token = tokenGenerator.generateToken("alice", UserRole.MESERO);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_MESERO")));
    }

    @Test
    void doFilterInternal_withCocineroRoleToken_setsRoleCocineroAuthority() throws Exception {
        String token = tokenGenerator.generateToken("bob", UserRole.COCINERO);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_COCINERO")));
    }
}
