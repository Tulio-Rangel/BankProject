package com.tulio.banksofka.service;

import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private BankAccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_ShouldGenerateNumberAndSaveAccount() {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(accountRepository.save(any(BankAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        BankAccount account = accountService.createAccount(user);

        // Assert
        assertNotNull(account.getAccountNumber());
        assertEquals(0.0, account.getBalance());
        assertEquals(user, account.getUser());
        verify(accountRepository).save(any(BankAccount.class));
    }

    @Test
    void makeDeposit_ShouldUpdateBalanceAndCreateTransaction() {
        // Arrange
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setBalance(100.0);
        account.setTransactions(new ArrayList<>());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(BankAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        accountService.makeDeposit(1L, 50.0);

        // Assert
        assertEquals(150.0, account.getBalance());
        assertEquals(1, account.getTransactions().size());
        assertEquals("DEPOSIT", account.getTransactions().get(0).getType());
        assertEquals(50.0, account.getTransactions().get(0).getAmount());
    }

    @Test
    void makeWithdrawal_WithSufficientFunds_ShouldUpdateBalanceAndCreateTransaction() {
        // Arrange
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setBalance(100.0);
        account.setTransactions(new ArrayList<>());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(BankAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        accountService.makeWithdrawal(1L, 50.0);

        // Assert
        assertEquals(50.0, account.getBalance());
        assertEquals(1, account.getTransactions().size());
        assertEquals("WITHDRAWAL", account.getTransactions().get(0).getType());
    }

    @Test
    void makeWithdrawal_WithInsufficientFunds_ShouldThrowException() {
        // Arrange
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setBalance(30.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                accountService.makeWithdrawal(1L, 50.0)
        );
    }
}
