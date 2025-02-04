package com.tulio.banksofka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.service.AccountService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private BankAccount testAccount;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Test User", "password", "test@test.com");
        testAccount = new BankAccount();
        testAccount.setId(1L);
        testAccount.setAccountNumber("123456");
        testAccount.setBalance(1000.0);
        testAccount.setUser(testUser);
    }

    @Test
    @WithMockUser
    void createUser_ShouldReturnCreatedAccount() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(accountService.createAccount(any(User.class))).thenReturn(testAccount);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456"));
    }

    @Test
    @WithMockUser
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);
        when(userService.updateUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @WithMockUser
    void deleteUser_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getUserAccounts_ShouldReturnAccounts() throws Exception {
        testUser.setAccounts(Arrays.asList(testAccount));
        when(userService.findById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("123456"));
    }
}