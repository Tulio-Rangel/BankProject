package com.tulio.banksofka.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalanceDTOTest {
    @Test
    void testNewBalanceDTO() {
        // Arrange and Act
        BalanceDTO actualBalanceDTO = new BalanceDTO("42", 10.0d);

        // Assert
        assertEquals("42", actualBalanceDTO.getAccountNumber());
        assertEquals(10.0d, actualBalanceDTO.getBalance().doubleValue());
    }

    @Test
    void testEquals() {
        // Crear dos objetos BalanceDTO con los mismos datos
        BalanceDTO balanceDTO1 = new BalanceDTO("123456", 1000.0);
        BalanceDTO balanceDTO2 = new BalanceDTO("123456", 1000.0);

        // Verificar que son iguales
        assertEquals(balanceDTO1, balanceDTO2, "Los objetos BalanceDTO con los mismos datos deben ser iguales");

        // Crear un objeto BalanceDTO con datos diferentes
        BalanceDTO balanceDTO3 = new BalanceDTO("654321", 500.0);

        // Verificar que no son iguales
        assertNotEquals(balanceDTO1, balanceDTO3, "Los objetos BalanceDTO con datos diferentes no deben ser iguales");

        // Verificar que un objeto BalanceDTO no es igual a un objeto de otro tipo
        assertNotEquals(balanceDTO1, new Object(), "Un objeto BalanceDTO no debe ser igual a un objeto de otro tipo");

        // Verificar que un objeto BalanceDTO no es igual a null
        assertNotEquals(null, balanceDTO1, "Un objeto BalanceDTO no debe ser igual a null");
    }

    @Test
    void testHashCode() {
        // Crear dos objetos BalanceDTO con los mismos datos
        BalanceDTO balanceDTO1 = new BalanceDTO("123456", 1000.0);
        BalanceDTO balanceDTO2 = new BalanceDTO("123456", 1000.0);

        // Verificar que tienen el mismo hashCode
        assertEquals(balanceDTO1.hashCode(), balanceDTO2.hashCode(), "Los objetos BalanceDTO con los mismos datos deben tener el mismo hashCode");

        // Crear un objeto BalanceDTO con datos diferentes
        BalanceDTO balanceDTO3 = new BalanceDTO("654321", 500.0);

        // Verificar que tienen un hashCode diferente
        assertNotEquals(balanceDTO1.hashCode(), balanceDTO3.hashCode(), "Los objetos BalanceDTO con datos diferentes deben tener un hashCode diferente");
    }

    @Test
    void testToString() {
        // Crear un objeto BalanceDTO
        BalanceDTO balanceDTO = new BalanceDTO("123456", 1000.0);

        // Verificar que el método toString devuelve la representación esperada
        String expectedToString = "BalanceDTO(accountNumber=123456, balance=1000.0)";
        assertEquals(expectedToString, balanceDTO.toString(), "El método toString debe devolver la representación esperada");
    }

    @Test
    void testCanEqual() {
        // Crear un objeto BalanceDTO
        BalanceDTO balanceDTO1 = new BalanceDTO("123456", 1000.0);

        // Crear otro objeto BalanceDTO con los mismos datos
        BalanceDTO balanceDTO2 = new BalanceDTO("123456", 1000.0);

        // Verificar que balanceDTO1 puede ser igual a balanceDTO2
        assertTrue(balanceDTO1.canEqual(balanceDTO2), "balanceDTO1 debe poder ser igual a balanceDTO2");

        // Crear un objeto de otro tipo
        Object otherObject = new Object();

        // Verificar que balanceDTO1 no puede ser igual a un objeto de otro tipo
        assertFalse(balanceDTO1.canEqual(otherObject), "balanceDTO1 no debe poder ser igual a un objeto de otro tipo");
    }
}
