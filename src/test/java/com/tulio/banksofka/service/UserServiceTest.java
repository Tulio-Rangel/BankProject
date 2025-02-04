package com.tulio.banksofka.service;

import com.tulio.banksofka.model.User;
import com.tulio.banksofka.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Test User", "password", "test@test.com");
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser);

        assertNotNull(result);
        assertEquals(testUser.getName(), result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(testUser);

        assertNotNull(result);
        assertEquals(testUser.getName(), result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(1L));
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));

        User result = userService.findByEmail("test@test.com");

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
    }

    @Test
    void deleteUser_ShouldCallRepository() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}