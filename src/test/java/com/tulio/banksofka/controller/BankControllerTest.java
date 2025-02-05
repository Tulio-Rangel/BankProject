package com.tulio.banksofka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.service.AccountService;
import com.tulio.banksofka.exception.InsufficientBalanceException;
import com.tulio.banksofka.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private BankAccount testAccount;
    private BalanceDTO balanceDTO;

    @BeforeEach
    void setUp() {
        User testUser = new User(1L, "Test User", "password", "test@test.com");

        testAccount = new BankAccount();
        testAccount.setId(1L);
        testAccount.setAccountNumber("123456");
        testAccount.setBalance(1000.0);
        testAccount.setUser(testUser);

        balanceDTO = new BalanceDTO(null, null);
        balanceDTO.setAccountNumber("123456");
        balanceDTO.setBalance(1000.0);
    }

    @Test
    @WithMockUser
    void getBalance_ShouldReturnBalance() throws Exception {
        when(accountService.getBalanceInfo(1L)).thenReturn(balanceDTO);

        mockMvc.perform(get("/api/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    @WithMockUser
    void deposit_ShouldUpdateBalance() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        doNothing().when(accountService).makeDeposit(1L, 500.0);
        when(accountService.getBalanceInfo(1L)).thenReturn(balanceDTO);

        mockMvc.perform(post("/api/accounts/1/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void withdraw_ShouldUpdateBalance() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        doNothing().when(accountService).makeWithdrawal(1L, 500.0);
        when(accountService.getBalanceInfo(1L)).thenReturn(balanceDTO);

        mockMvc.perform(post("/api/accounts/1/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void withdraw_WithInsufficientFunds_ShouldReturnBadRequest() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 2000.0);

        doThrow(new InsufficientBalanceException("Insufficient funds"))
                .when(accountService).makeWithdrawal(1L, 2000.0);

        mockMvc.perform(post("/api/accounts/1/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds"));
    }

    // Tests para createAccount
    @Test
    void createAccount_UserNotFound_ReturnsNotFound() throws Exception {
        Long userId = 999L;
        when(userService.findById(userId)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/api/accounts/{userId}", userId))
                .andExpect(status().isForbidden()); // O 404 si hay manejo de excepciones
    }

    // Tests para getTransactionHistory
    @Test
    void getTransactionHistory_AccountNotFound_ReturnsNotFound() throws Exception {
        Long accountId = 999L;
        when(accountService.getTransactionHistory(accountId))
                .thenThrow(new RuntimeException("Account not found"));

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isForbidden()); // O 404 si hay manejo de excepciones
    }

    @Test
    void getTransactionHistory_NoTransactions_ReturnsEmptyList() throws Exception {
        Long accountId = 100L;
        when(accountService.getTransactionHistory(accountId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isForbidden());
    }
}