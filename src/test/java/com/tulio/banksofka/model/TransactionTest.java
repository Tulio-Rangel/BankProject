package com.tulio.banksofka.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TransactionTest {
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        Transaction actualTransaction = new Transaction();
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        user.setPassword("iloveyou");
        BankAccount account = new BankAccount();
        account.setAccountNumber("42");
        account.setBalance(10.0d);
        account.setId(1L);
        account.setTransactions(new ArrayList<>());
        account.setUser(user);
        actualTransaction.setAccount(account);
        actualTransaction.setAmount(10.0d);
        LocalDateTime date = LocalDate.of(1970, 1, 1).atStartOfDay();
        actualTransaction.setDate(date);
        actualTransaction.setId(1L);
        actualTransaction.setType("Type");
        String actualToStringResult = actualTransaction.toString();
        BankAccount actualAccount = actualTransaction.getAccount();
        Double actualAmount = actualTransaction.getAmount();
        LocalDateTime actualDate = actualTransaction.getDate();
        Long actualId = actualTransaction.getId();

        // Assert that nothing has changed
        assertEquals(
                "Transaction(id=1, type=Type, amount=10.0, date=1970-01-01T00:00, account=BankAccount(id=1, accountNumber=42,"
                        + " balance=10.0, user=User(id=1, name=Name, email=jane.doe@example.org, password=iloveyou, accounts=[]),"
                        + " transactions=[]))",
                actualToStringResult);
        assertEquals("Type", actualTransaction.getType());
        assertEquals(10.0d, actualAmount.doubleValue());
        assertEquals(1L, actualId.longValue());
        assertSame(account, actualAccount);
        assertSame(date, actualDate);
    }
}