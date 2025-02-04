package com.tulio.banksofka.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void setBalance_ShouldUpdateBalance() {
        BankAccount account = new BankAccount();
        account.setBalance(1000.0);
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    void setAccountNumber_ShouldUpdateAccountNumber() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("123456");
        assertEquals("123456", account.getAccountNumber());
    }

    @Test
    void setUser_ShouldUpdateUser() {
        BankAccount account = new BankAccount();
        User user = new User(1L, "Test User", "password", "test@test.com");
        account.setUser(user);
        assertEquals(user, account.getUser());
    }
}