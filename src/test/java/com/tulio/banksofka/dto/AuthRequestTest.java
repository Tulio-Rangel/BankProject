package com.tulio.banksofka.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AuthRequestTest {

    @Test
    void testSettersAndGetters() {
        AuthRequest authRequest = new AuthRequest();

        authRequest.setEmail("test@example.com");
        authRequest.setPassword("securePassword123");

        assertThat(authRequest.getEmail()).isEqualTo("test@example.com");
        assertThat(authRequest.getPassword()).isEqualTo("securePassword123");
    }

    @Test
    void testEqualsAndHashCode() {
        AuthRequest auth1 = new AuthRequest();
        auth1.setEmail("user@example.com");
        auth1.setPassword("password123");

        AuthRequest auth2 = new AuthRequest();
        auth2.setEmail("user@example.com");
        auth2.setPassword("password123");

        assertThat(auth1).isEqualTo(auth2); // Verifica que son iguales
        assertThat(auth1.hashCode()).hasSameHashCodeAs(auth2.hashCode()); // Verifica que el hashCode es el mismo
    }

    @Test
    void testToString() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        String toStringResult = authRequest.toString();

        assertThat(toStringResult).contains("test@example.com", "password");
    }
}
