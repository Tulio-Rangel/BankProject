package com.tulio.banksofka.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class BankAccountTest {
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        BankAccount actualBankAccount = new BankAccount();
        actualBankAccount.setAccountNumber("42");
        actualBankAccount.setBalance(10.0d);
        actualBankAccount.setId(1L);
        ArrayList<Transaction> transactions = new ArrayList<>();
        actualBankAccount.setTransactions(transactions);
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        user.setPassword("iloveyou");
        actualBankAccount.setUser(user);
        String actualToStringResult = actualBankAccount.toString();
        String actualAccountNumber = actualBankAccount.getAccountNumber();
        Double actualBalance = actualBankAccount.getBalance();
        Long actualId = actualBankAccount.getId();
        List<Transaction> actualTransactions = actualBankAccount.getTransactions();
        User actualUser = actualBankAccount.getUser();

        // Assert that nothing has changed
        assertEquals("42", actualAccountNumber);
        assertEquals(
                "BankAccount(id=1, accountNumber=42, balance=10.0, user=User(id=1, name=Name, email=jane.doe@example.org,"
                        + " password=iloveyou, accounts=[]), transactions=[])",
                actualToStringResult);
        assertEquals(10.0d, actualBalance.doubleValue());
        assertEquals(1L, actualId.longValue());
        assertTrue(actualTransactions.isEmpty());
        assertSame(user, actualUser);
        assertSame(transactions, actualTransactions);
    }
}
