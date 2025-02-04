package com.tulio.banksofka.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AuditTransactionRequestTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        AuditTransactionRequest request = new AuditTransactionRequest();
        String userId = "user123";
        Double initialBalance = 1000.0;
        Double amount = 200.0;
        Double finalBalance = 1200.0;

        // Act
        request.setUserId(userId);
        request.setInitialBalance(initialBalance);
        request.setAmount(amount);
        request.setFinalBalance(finalBalance);

        // Assert
        assertEquals(userId, request.getUserId(), "El userId no coincide");
        assertEquals(initialBalance, request.getInitialBalance(), "El initialBalance no coincide");
        assertEquals(amount, request.getAmount(), "El amount no coincide");
        assertEquals(finalBalance, request.getFinalBalance(), "El finalBalance no coincide");
    }

    @Test
    void testDefaultValues() {
        // Arrange
        AuditTransactionRequest request = new AuditTransactionRequest();

        // Assert
        assertEquals(null, request.getUserId(), "El userId no es null por defecto");
        assertEquals(null, request.getInitialBalance(), "El initialBalance no es null por defecto");
        assertEquals(null, request.getAmount(), "El amount no es null por defecto");
        assertEquals(null, request.getFinalBalance(), "El finalBalance no es null por defecto");
    }
}