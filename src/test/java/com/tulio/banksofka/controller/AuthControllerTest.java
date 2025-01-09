package com.tulio.banksofka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulio.banksofka.dto.AuthRequest;
import com.tulio.banksofka.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void authenticate_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        UserDetails userDetails = User.withUsername("test@test.com")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)));
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
