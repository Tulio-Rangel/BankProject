package com.tulio.banksofka.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testEquals() {
        // Crear dos objetos TransactionDTO con los mismos datos
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO transactionDTO1 = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));
        TransactionDTO transactionDTO2 = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));

        // Verificar que son iguales
        assertEquals(transactionDTO1, transactionDTO2, "Los objetos TransactionDTO con los mismos datos deben ser iguales");

        // Crear un objeto TransactionDTO con datos diferentes
        TransactionDTO transactionDTO3 = new TransactionDTO(new Transaction(2L, "WITHDRAWAL", 50.0, now, null));

        // Verificar que no son iguales
        assertNotEquals(transactionDTO1, transactionDTO3, "Los objetos TransactionDTO con datos diferentes no deben ser iguales");

        // Verificar que un objeto TransactionDTO no es igual a un objeto de otro tipo
        assertNotEquals(transactionDTO1, new Object(), "Un objeto TransactionDTO no debe ser igual a un objeto de otro tipo");

        // Verificar que un objeto TransactionDTO no es igual a null
        assertNotEquals(null, transactionDTO1, "Un objeto TransactionDTO no debe ser igual a null");
    }

    @Test
    void testHashCode() {
        // Crear dos objetos TransactionDTO con los mismos datos
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO transactionDTO1 = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));
        TransactionDTO transactionDTO2 = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));

        // Verificar que tienen el mismo hashCode
        assertEquals(transactionDTO1.hashCode(), transactionDTO2.hashCode(), "Los objetos TransactionDTO con los mismos datos deben tener el mismo hashCode");

        // Crear un objeto TransactionDTO con datos diferentes
        TransactionDTO transactionDTO3 = new TransactionDTO(new Transaction(2L, "WITHDRAWAL", 50.0, now, null));

        // Verificar que tienen un hashCode diferente
        assertNotEquals(transactionDTO1.hashCode(), transactionDTO3.hashCode(), "Los objetos TransactionDTO con datos diferentes deben tener un hashCode diferente");
    }

    @Test
    void testToString() {
        // Crear un objeto TransactionDTO
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO transactionDTO = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));

        // Verificar que el método toString devuelve la representación esperada
        String expectedToString = "TransactionDTO(id=1, type=DEPOSIT, amount=200.0, date=" + now + ")";
        assertEquals(expectedToString, transactionDTO.toString(), "El método toString debe devolver la representación esperada");
    }

    @Test
    void testSetters() {
        // Crear un objeto TransactionDTO
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO transactionDTO = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));

        // Cambiar los valores usando los setters
        transactionDTO.setId(2L);
        transactionDTO.setType("WITHDRAWAL");
        transactionDTO.setAmount(50.0);
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        transactionDTO.setDate(newDate);

        // Verificar que los valores se han actualizado correctamente
        assertEquals(2L, transactionDTO.getId(), "El ID debe ser 2");
        assertEquals("WITHDRAWAL", transactionDTO.getType(), "El tipo debe ser WITHDRAWAL");
        assertEquals(50.0, transactionDTO.getAmount(), "El monto debe ser 50.0");
        assertEquals(newDate, transactionDTO.getDate(), "La fecha debe ser la nueva fecha");
    }

    @Test
    void testCanEqual() {
        // Crear un objeto TransactionDTO
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO transactionDTO1 = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));

        // Crear otro objeto TransactionDTO con los mismos datos
        TransactionDTO transactionDTO2 = new TransactionDTO(new Transaction(1L, "DEPOSIT", 200.0, now, null));

        // Verificar que transactionDTO1 puede ser igual a transactionDTO2
        assertTrue(transactionDTO1.canEqual(transactionDTO2), "transactionDTO1 debe poder ser igual a transactionDTO2");

        // Crear un objeto de otro tipo
        Object otherObject = new Object();

        // Verificar que transactionDTO1 no puede ser igual a un objeto de otro tipo
        assertFalse(transactionDTO1.canEqual(otherObject), "transactionDTO1 no debe poder ser igual a un objeto de otro tipo");
    }
}
