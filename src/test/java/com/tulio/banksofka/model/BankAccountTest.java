package com.tulio.banksofka.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void testEquals() {
        // Crear dos cuentas bancarias con los mismos datos
        User user1 = new User(1L, "John Doe", "password123", "john@example.com");
        List<Transaction> transactions1 = new ArrayList<>();
        BankAccount account1 = new BankAccount(1L, "123456", 1000.0, user1, transactions1);
        BankAccount account2 = new BankAccount(1L, "123456", 1000.0, user1, transactions1);

        // Verificar que son iguales
        assertEquals(account1, account2, "Las cuentas bancarias con los mismos datos deben ser iguales");

        // Crear una cuenta bancaria con datos diferentes
        BankAccount account3 = new BankAccount(2L, "654321", 500.0, user1, transactions1);

        // Verificar que no son iguales
        assertNotEquals(account1, account3, "Las cuentas bancarias con datos diferentes no deben ser iguales");

        // Verificar que una cuenta bancaria no es igual a un objeto de otro tipo
        assertNotEquals(account1, new Object(), "Una cuenta bancaria no debe ser igual a un objeto de otro tipo");

        // Verificar que una cuenta bancaria no es igual a null
        assertNotEquals(null, account1, "Una cuenta bancaria no debe ser igual a null");
    }

    @Test
    void testWithBalance() {
        // Crear una cuenta bancaria
        User user1 = new User(1L, "John Doe", "password123", "john@example.com");
        List<Transaction> transactions1 = new ArrayList<>();
        BankAccount originalAccount = new BankAccount(1L, "123456", 1000.0, user1, transactions1);

        // Actualizar el balance usando withBalance
        Double newBalance = 1500.0;
        BankAccount updatedAccount = originalAccount.withBalance(newBalance);

        // Verificar que la cuenta original no ha cambiado
        assertEquals(1000.0, originalAccount.getBalance(), "El balance de la cuenta original no debe cambiar");

        // Verificar que la nueva cuenta tiene el balance actualizado
        assertEquals(newBalance, updatedAccount.getBalance(), "El balance de la nueva cuenta debe ser el actualizado");

        // Verificar que los demás campos no han cambiado
        assertEquals(originalAccount.getId(), updatedAccount.getId(), "El ID no debe cambiar");
        assertEquals(originalAccount.getAccountNumber(), updatedAccount.getAccountNumber(), "El número de cuenta no debe cambiar");
        assertEquals(originalAccount.getUser(), updatedAccount.getUser(), "El usuario no debe cambiar");
        assertEquals(originalAccount.getTransactions(), updatedAccount.getTransactions(), "Las transacciones no deben cambiar");
    }
}