package com.tulio.banksofka.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AuthResponseTest {

    @Test
    void testConstructorAndGetters() {
        AuthResponse authResponse = new AuthResponse("token123", "John Doe", "john.doe@example.com", 1L);

        assertThat(authResponse.getToken()).isEqualTo("token123");
        assertThat(authResponse.getName()).isEqualTo("John Doe");
        assertThat(authResponse.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(authResponse.getId()).isEqualTo(1L);
    }

    @Test
    void testSetters() {
        AuthResponse authResponse = new AuthResponse("token123", "John Doe", "john.doe@example.com", 1L);

        authResponse.setToken("newToken456");
        authResponse.setName("Jane Doe");
        authResponse.setEmail("jane.doe@example.com");
        authResponse.setId(2L);

        assertThat(authResponse.getToken()).isEqualTo("newToken456");
        assertThat(authResponse.getName()).isEqualTo("Jane Doe");
        assertThat(authResponse.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(authResponse.getId()).isEqualTo(2L);
    }

    @Test
    void testEqualsAndHashCode() {
        AuthResponse auth1 = new AuthResponse("token123", "John Doe", "john.doe@example.com", 1L);
        AuthResponse auth2 = new AuthResponse("token123", "John Doe", "john.doe@example.com", 1L);

        assertThat(auth1).isEqualTo(auth2); // Verifica que equals funciona
        assertThat(auth1.hashCode()).hasSameHashCodeAs(auth2.hashCode()); // Verifica que hashCode es consistente
    }

    @Test
    void testToString() {
        AuthResponse authResponse = new AuthResponse("token123", "John Doe", "john.doe@example.com", 1L);

        String toStringResult = authResponse.toString();

        assertThat(toStringResult).contains("token123", "John Doe", "john.doe@example.com", "1");
    }
}
