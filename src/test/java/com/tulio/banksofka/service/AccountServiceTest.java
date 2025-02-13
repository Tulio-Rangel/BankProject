package com.tulio.banksofka.service;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;
import com.tulio.banksofka.repository.BankAccountRepository;
import com.tulio.banksofka.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private BankAccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private User testUser;
    private BankAccount testAccount;
    private BankAccount destinationAccount;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Test User", "password", "test@test.com");
        testAccount = new BankAccount();
        testAccount.setId(1L);
        testAccount.setAccountNumber("123456");
        testAccount.setBalance(1000.0);
        testAccount.setUser(testUser);

        destinationAccount = new BankAccount();
        destinationAccount.setId(2L);
        destinationAccount.setAccountNumber("789012");
        destinationAccount.setBalance(500.0);
        destinationAccount.setUser(testUser);
    }

    @SuppressWarnings("unchecked")
    private void setupWebClientMock() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.empty());
    }

    @Test
    void deposit_ShouldIncreaseBalance() {
        setupWebClientMock();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> {
            BankAccount savedAccount = invocation.getArgument(0);
            testAccount.setBalance(savedAccount.getBalance());
            return testAccount;
        });
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            savedTransaction.setDate(LocalDateTime.now());
            return savedTransaction;
        });

        accountService.makeDeposit(1L, 500.0);

        assertEquals(1500.0, testAccount.getBalance());
        verify(accountRepository).save(any(BankAccount.class));
        verify(transactionRepository).save(any(Transaction.class));
        verify(requestBodyUriSpec).uri("/api/audit/deposits");
        verify(requestBodyUriSpec).bodyValue(any());
    }

    @Test
    void withdraw_WithSufficientFunds_ShouldDecreaseBalance() {
        setupWebClientMock();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> {
            BankAccount savedAccount = invocation.getArgument(0);
            testAccount.setBalance(savedAccount.getBalance());
            return testAccount;
        });
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            savedTransaction.setDate(LocalDateTime.now());
            return savedTransaction;
        });

        accountService.makeWithdrawal(1L, 500.0);

        assertEquals(500.0, testAccount.getBalance());
        verify(accountRepository).save(any(BankAccount.class));
        verify(transactionRepository).save(any(Transaction.class));
        verify(requestBodyUriSpec).uri("/api/audit/withdrawals");
        verify(requestBodyUriSpec).bodyValue(any());
    }

    @Test
    void withdraw_WithInsufficientFunds_ShouldThrowException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        assertThrows(RuntimeException.class, () -> {
            accountService.makeWithdrawal(1L, 2000.0);
        });

        verify(accountRepository, never()).save(any(BankAccount.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void getBalance_ShouldReturnCurrentBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        BalanceDTO balance = accountService.getBalanceInfo(1L);

        assertEquals(testAccount.getBalance(), balance.getBalance());
        assertEquals(testAccount.getAccountNumber(), balance.getAccountNumber());
    }
}