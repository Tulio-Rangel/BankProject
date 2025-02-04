package com.tulio.banksofka.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BalanceDTOTest {
    @Test
    void testNewBalanceDTO() {
        // Arrange and Act
        BalanceDTO actualBalanceDTO = new BalanceDTO("42", 10.0d);

        // Assert
        assertEquals("42", actualBalanceDTO.getAccountNumber());
        assertEquals(10.0d, actualBalanceDTO.getBalance().doubleValue());
    }
}
