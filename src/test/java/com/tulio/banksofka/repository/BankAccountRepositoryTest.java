package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BankAccountRepositoryTest {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private BankAccount testAccount;

    @BeforeEach
    void setUp() {
        // Arrange - Crear usuario de prueba
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        // Arrange - Crear cuenta de prueba
        testAccount = new BankAccount();
        testAccount.setAccountNumber("1234567890");
        testAccount.setBalance(100.0);
        testAccount.setUser(testUser);
        testAccount = bankAccountRepository.save(testAccount);
    }

    @Test
    void existsByAccountNumber_WhenAccountExists_ShouldReturnTrue() {
        // Act
        boolean exists = bankAccountRepository.existsByAccountNumber("1234567890");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByAccountNumber_WhenAccountDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean exists = bankAccountRepository.existsByAccountNumber("0987654321");

        // Assert
        assertFalse(exists);
    }

    @Test
    void save_ShouldPersistBankAccount() {
        // Arrange
        BankAccount newAccount = new BankAccount();
        newAccount.setAccountNumber("9876543210");
        newAccount.setBalance(200.0);
        newAccount.setUser(testUser);

        // Act
        BankAccount savedAccount = bankAccountRepository.save(newAccount);

        // Assert
        assertNotNull(savedAccount.getId());
        assertEquals("9876543210", savedAccount.getAccountNumber());
        assertEquals(200.0, savedAccount.getBalance());
        assertEquals(testUser.getId(), savedAccount.getUser().getId());
    }

    @Test
    void findById_WhenAccountExists_ShouldReturnAccount() {
        // Act
        Optional<BankAccount> foundAccount = bankAccountRepository.findById(testAccount.getId());

        // Assert
        assertTrue(foundAccount.isPresent());
        assertEquals(testAccount.getAccountNumber(), foundAccount.get().getAccountNumber());
        assertEquals(testAccount.getBalance(), foundAccount.get().getBalance());
    }

    @Test
    void findById_WhenAccountDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<BankAccount> foundAccount = bankAccountRepository.findById(999L);

        // Assert
        assertTrue(foundAccount.isEmpty());
    }

    @Test
    void delete_ShouldRemoveBankAccount() {
        // Act
        bankAccountRepository.delete(testAccount);
        Optional<BankAccount> deletedAccount = bankAccountRepository.findById(testAccount.getId());

        // Assert
        assertTrue(deletedAccount.isEmpty());
    }

    @Test
    void save_WhenUpdatingExistingAccount_ShouldUpdateFields() {
        // Arrange
        testAccount.setBalance(300.0);

        // Act
        BankAccount updatedAccount = bankAccountRepository.save(testAccount);

        // Assert
        assertEquals(300.0, updatedAccount.getBalance());
        assertEquals(testAccount.getId(), updatedAccount.getId());
        assertEquals(testAccount.getAccountNumber(), updatedAccount.getAccountNumber());
    }
}