package com.tulio.banksofka.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;
import com.tulio.banksofka.model.User;

class TransactionDTOTest {
    @Test
    void testNewTransactionDTO() {
        // Arrange
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

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(10.0d);
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        transaction.setDate(ofResult.atStartOfDay());
        transaction.setId(1L);
        transaction.setType("Type");

        // Act
        TransactionDTO actualTransactionDTO = new TransactionDTO(transaction);

        // Assert
        LocalDateTime date = actualTransactionDTO.getDate();
        assertEquals("00:00", date.toLocalTime().toString());
        LocalDate toLocalDateResult = date.toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        assertEquals("Type", actualTransactionDTO.getType());
        assertEquals(10.0d, actualTransactionDTO.getAmount().doubleValue());
        assertEquals(1L, actualTransactionDTO.getId().longValue());
        assertSame(ofResult, toLocalDateResult);
    }
}
