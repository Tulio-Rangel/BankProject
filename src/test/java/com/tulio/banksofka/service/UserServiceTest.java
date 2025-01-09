package com.tulio.banksofka.service;

import com.tulio.banksofka.model.User;
import com.tulio.banksofka.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setName("Test User");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.createUser(user);

        // Assert
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
        assertEquals("test@test.com", savedUser.getEmail());
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findById(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.findById(1L));
    }
}