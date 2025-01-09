package com.tulio.banksofka.controller;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.security.JwtAuthorizationFilter;
import com.tulio.banksofka.security.SecurityConfigTest;
import com.tulio.banksofka.service.AccountService;
import com.tulio.banksofka.service.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
@Import(SecurityConfigTest.class)
class BankControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AccountService accountService;
    @MockBean
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        doNothing().when(jwtAuthorizationFilter).doFilter(any(), any(), any());
    }

    @Test
    void getBalance_ShouldReturnBalanceInfo() throws Exception {
        // Arrange
        BalanceDTO balanceDTO = new BalanceDTO("1234567890", 100.0);
        when(accountService.getBalanceInfo(1L)).thenReturn(balanceDTO);

        // Act & Assert
        mockMvc.perform(get("/api/accounts/1/balance"))
                .andExpect(status().isOk());
    }

    @Test
    void makeDeposit_ShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(accountService).makeDeposit(1L, 100.0);

        // Act & Assert
        mockMvc.perform(post("/api/accounts/1/deposit")
                        .param("amount", "100.0"))
                .andExpect(status().isOk());
    }
}