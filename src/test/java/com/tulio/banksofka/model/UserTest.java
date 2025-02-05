package com.tulio.banksofka.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        User actualUser = new User();
        ArrayList<BankAccount> accounts = new ArrayList<>();
        actualUser.setAccounts(accounts);
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setId(1L);
        actualUser.setName("Name");
        actualUser.setPassword("iloveyou");
        String actualToStringResult = actualUser.toString();
        List<BankAccount> actualAccounts = actualUser.getAccounts();
        String actualEmail = actualUser.getEmail();
        Long actualId = actualUser.getId();
        String actualName = actualUser.getName();

        // Assert that nothing has changed
        assertEquals("Name", actualName);
        assertEquals("User(id=1, name=Name, email=jane.doe@example.org, password=iloveyou, accounts=[])",
                actualToStringResult);
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals(1L, actualId.longValue());
        assertTrue(actualAccounts.isEmpty());
        assertSame(accounts, actualAccounts);
    }

    @Test
    void testEquals() {
        // Crear dos usuarios con los mismos datos
        User user1 = new User(1L, "John Doe", "password123", "john@example.com");
        User user2 = new User(1L, "John Doe", "password123", "john@example.com");

        // Verificar que son iguales
        assertEquals(user1, user2, "Los usuarios con los mismos datos deben ser iguales");

        // Crear un usuario con datos diferentes
        User user3 = new User(2L, "Jane Doe", "password456", "jane@example.com");

        // Verificar que no son iguales
        assertNotEquals(user1, user3, "Los usuarios con datos diferentes no deben ser iguales");

        // Verificar que un usuario no es igual a un objeto de otro tipo
        assertNotEquals(user1, new Object(), "Un usuario no debe ser igual a un objeto de otro tipo");

        // Verificar que un usuario no es igual a null
        assertNotEquals(null, user1, "Un usuario no debe ser igual a null");
    }

    @Test
    void testHashCode() {
        // Crear dos usuarios con los mismos datos
        User user1 = new User(1L, "John Doe", "password123", "john@example.com");
        User user2 = new User(1L, "John Doe", "password123", "john@example.com");

        // Verificar que tienen el mismo hashCode
        assertEquals(user1.hashCode(), user2.hashCode(), "Los usuarios con los mismos datos deben tener el mismo hashCode");

        // Crear un usuario con datos diferentes
        User user3 = new User(2L, "Jane Doe", "password456", "jane@example.com");

        // Verificar que tienen un hashCode diferente
        assertNotEquals(user1.hashCode(), user3.hashCode(), "Los usuarios con datos diferentes deben tener un hashCode diferente");
    }

    @Test
    void testCanEqual() {
        // Crear un usuario
        User user1 = new User(1L, "John Doe", "password123", "john@example.com");

        // Crear otro usuario con los mismos datos
        User user2 = new User(1L, "John Doe", "password123", "john@example.com");

        // Verificar que user1 puede ser igual a user2
        assertTrue(user1.canEqual(user2), "user1 debe poder ser igual a user2");

        // Crear un objeto de otro tipo
        Object otherObject = new Object();

        // Verificar que user1 no puede ser igual a un objeto de otro tipo
        assertFalse(user1.canEqual(otherObject), "user1 no debe poder ser igual a un objeto de otro tipo");
    }

    @Test
    void testEqualsWithAccounts() {
        // Crear dos usuarios con los mismos datos
        User user1 = new User(1L, "John Doe", "password123", "john@example.com");
        User user2 = new User(1L, "John Doe", "password123", "john@example.com");

        // Crear una lista de cuentas para user1
        List<BankAccount> accounts1 = new ArrayList<>();
        accounts1.add(new BankAccount());
        user1.setAccounts(accounts1);

        // Crear una lista de cuentas para user2
        List<BankAccount> accounts2 = new ArrayList<>();
        accounts2.add(new BankAccount());
        user2.setAccounts(accounts2);

        // Verificar que son iguales a pesar de tener cuentas
        assertEquals(user1, user2, "Los usuarios con los mismos datos deben ser iguales, incluso si tienen cuentas");

    }
}