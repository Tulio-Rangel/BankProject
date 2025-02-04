package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Para manejar entidades en el contexto de persistencia

    @Autowired
    private TransactionRepository transactionRepository; // Repositorio a probar

    @Test
    void testSaveTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        transaction.setType("DEPOSIT");

        // Act
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Assert
        assertNotNull(savedTransaction.getId(), "El ID de la transacción no debe ser nulo después de guardar");
        assertEquals(100.0, savedTransaction.getAmount(), "El monto de la transacción no coincide");
        assertEquals("DEPOSIT", savedTransaction.getType(), "El tipo de transacción no coincide");
    }

    @Test
    void testFindById() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(200.0);
        transaction.setType("WITHDRAWAL");
        entityManager.persist(transaction); // Guardar la transacción en la base de datos en memoria
        entityManager.flush();

        // Act
        Optional<Transaction> foundTransaction = transactionRepository.findById(transaction.getId());

        // Assert
        assertTrue(foundTransaction.isPresent(), "La transacción debe existir en la base de datos");
        assertEquals(200.0, foundTransaction.get().getAmount(), "El monto de la transacción no coincide");
        assertEquals("WITHDRAWAL", foundTransaction.get().getType(), "El tipo de transacción no coincide");
    }

    @Test
    void testFindAll() {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(100.0);
        transaction1.setType("DEPOSIT");
        entityManager.persist(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(200.0);
        transaction2.setType("WITHDRAWAL");
        entityManager.persist(transaction2);

        entityManager.flush();

        // Act
        List<Transaction> transactions = transactionRepository.findAll();

        // Assert
        assertEquals(2, transactions.size(), "Debe haber 2 transacciones en la base de datos");
    }

    @Test
    void testDeleteTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(300.0);
        transaction.setType("TRANSFER");
        entityManager.persist(transaction);
        entityManager.flush();

        // Act
        transactionRepository.delete(transaction);
        Optional<Transaction> deletedTransaction = transactionRepository.findById(transaction.getId());

        // Assert
        assertFalse(deletedTransaction.isPresent(), "La transacción debe ser eliminada de la base de datos");
    }
}