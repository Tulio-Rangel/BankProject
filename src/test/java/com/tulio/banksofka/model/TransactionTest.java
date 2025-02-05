package com.tulio.banksofka.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testEquals() {
        // Crear dos transacciones con los mismos datos
        BankAccount account1 = new BankAccount(1L, "123456", 1000.0, new User(), new ArrayList<>());
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction1 = new Transaction(1L, "DEPOSIT", 200.0, now, account1);
        Transaction transaction2 = new Transaction(1L, "DEPOSIT", 200.0, now, account1);

        // Verificar que son iguales
        assertEquals(transaction1, transaction2, "Las transacciones con los mismos datos deben ser iguales");

        // Crear una transacción con datos diferentes
        Transaction transaction3 = new Transaction(2L, "WITHDRAWAL", 50.0, now, account1);

        // Verificar que no son iguales
        assertNotEquals(transaction1, transaction3, "Las transacciones con datos diferentes no deben ser iguales");

        // Verificar que una transacción no es igual a un objeto de otro tipo
        assertNotEquals(transaction1, new Object(), "Una transacción no debe ser igual a un objeto de otro tipo");

        // Verificar que una transacción no es igual a null
        assertNotEquals(null, transaction1, "Una transacción no debe ser igual a null");
    }

    @Test
    void testHashCode() {
        // Crear dos transacciones con los mismos datos
        BankAccount account1 = new BankAccount(1L, "123456", 1000.0, new User(), new ArrayList<>());
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction1 = new Transaction(1L, "DEPOSIT", 200.0, now, account1);
        Transaction transaction2 = new Transaction(1L, "DEPOSIT", 200.0, now, account1);

        // Verificar que tienen el mismo hashCode
        assertEquals(transaction1.hashCode(), transaction2.hashCode(), "Las transacciones con los mismos datos deben tener el mismo hashCode");

        // Crear una transacción con datos diferentes
        Transaction transaction3 = new Transaction(2L, "WITHDRAWAL", 50.0, now, account1);

        // Verificar que tienen un hashCode diferente
        assertNotEquals(transaction1.hashCode(), transaction3.hashCode(), "Las transacciones con datos diferentes deben tener un hashCode diferente");
    }

    @Test
    void testCanEqual() {
        // Crear una transacción
        BankAccount account1 = new BankAccount(1L, "123456", 1000.0, new User(), new ArrayList<>());
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction1 = new Transaction(1L, "DEPOSIT", 200.0, now, account1);

        // Crear otra transacción con los mismos datos
        Transaction transaction2 = new Transaction(1L, "DEPOSIT", 200.0, now, account1);

        // Verificar que transaction1 puede ser igual a transaction2
        assertTrue(transaction1.canEqual(transaction2), "transaction1 debe poder ser igual a transaction2");

        // Crear un objeto de otro tipo
        Object otherObject = new Object();

        // Verificar que transaction1 no puede ser igual a un objeto de otro tipo
        assertFalse(transaction1.canEqual(otherObject), "transaction1 no debe poder ser igual a un objeto de otro tipo");
    }

    @Test
    void testConstructorWithAllFields() {
        // Crear una cuenta bancaria
        BankAccount account1 = new BankAccount(1L, "123456", 1000.0, new User(), new ArrayList<>());

        // Crear una transacción usando el constructor con todos los campos
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction(1L, "DEPOSIT", 200.0, now, account1);

        // Verificar que los campos se han asignado correctamente
        assertEquals(1L, transaction.getId(), "El ID de la transacción debe ser 1");
        assertEquals("DEPOSIT", transaction.getType(), "El tipo de transacción debe ser DEPOSIT");
        assertEquals(200.0, transaction.getAmount(), "El monto de la transacción debe ser 200.0");
        assertEquals(now, transaction.getDate(), "La fecha de la transacción debe ser la esperada");
        assertEquals(account1, transaction.getAccount(), "La cuenta asociada debe ser la esperada");
    }
}