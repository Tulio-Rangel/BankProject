package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_ShouldReturnUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setName("Test User");
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@test.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("test@test.com", foundUser.get().getEmail());
    }

    @Test
    void save_ShouldPersistUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setName("Test User");

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("test@test.com", savedUser.getEmail());
    }
}
