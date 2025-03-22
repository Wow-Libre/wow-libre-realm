package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ItemsMachineTypeTest {

    @Test
    void testItemNamesNotEmpty() {
        for (ItemsMachineType item : ItemsMachineType.values()) {
            assertNotNull(item.getName(), "El nombre no debe ser nulo");
            assertFalse(item.getName().trim().isEmpty(), "El nombre no debe estar vacío");
        }
    }

    @Test
    void testExpectedItemCount() {
        int expectedItemCount = ItemsMachineType.values().length;
        assertEquals(expectedItemCount, ItemsMachineType.values().length, "Cantidad de ítems incorrecta");
    }
}
