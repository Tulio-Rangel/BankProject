package com.tulio.banksofka.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", "bWlDbGF2ZVNlY3JldGFTdXBlclNlZ3VyYVBhcmFFbEJhbmNvU29ma2EyMDI1");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Arrange
        UserDetails userDetails = User.withUsername("test@test.com")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        // Act
        String token = jwtUtil.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() throws InterruptedException {
        // Arrange
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        UserDetails userDetails = User.withUsername("test@test.com")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        // Act
        String token = jwtUtil.generateToken(userDetails);
        Thread.sleep(10);

        // Assert
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtUtil.extractUsername(token);
        });
    }
}
