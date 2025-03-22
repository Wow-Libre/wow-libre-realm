package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RolTypeTest {
    @Test
    void testGetById_ValidValues() {
        assertEquals(RolType.WOW_LIBRE, RolType.getById("WOW_LIBRE"));
        assertEquals(RolType.CLIENT, RolType.getById("CLIENT"));
        assertEquals(RolType.ADMIN, RolType.getById("ADMIN"));
    }

    @Test
    void testGetById_InvalidValue() {
        assertNull(RolType.getById("INVALID"));
    }

    @Test
    void testGetById_NullValue() {
        assertNull(RolType.getById(null));
    }
}
