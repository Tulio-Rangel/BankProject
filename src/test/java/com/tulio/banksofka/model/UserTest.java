package com.tulio.banksofka.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

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
}