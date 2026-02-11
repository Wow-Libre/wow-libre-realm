package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmulatorCoreTest {

    @Test
    void testGetById_ValidId() {
        assertEquals(EmulatorCore.TRINITY_CORE, EmulatorCore.getById(0));
        assertEquals(EmulatorCore.AZEROTH_CORE, EmulatorCore.getById(1));
        assertEquals(EmulatorCore.MANGOS, EmulatorCore.getById(2));
    }

    @Test
    void testGetById_InvalidId() {
        assertNull(EmulatorCore.getById(-1));
        assertNull(EmulatorCore.getById(999));
    }

    @Test
    void testEnumValues() {
        assertEquals(0, EmulatorCore.TRINITY_CORE.getId());
        assertEquals("TrinityCore", EmulatorCore.TRINITY_CORE.getName());

        assertEquals(1, EmulatorCore.AZEROTH_CORE.getId());
        assertEquals("AzerothCore", EmulatorCore.AZEROTH_CORE.getName());

        assertEquals(2, EmulatorCore.MANGOS.getId());
        assertEquals("Mangos", EmulatorCore.MANGOS.getName());
    }

    @Test
    void testGetByName_ValidName() {
        assertEquals(EmulatorCore.TRINITY_CORE, EmulatorCore.getByName("TrinityCore"));
        assertEquals(EmulatorCore.AZEROTH_CORE, EmulatorCore.getByName("AzerothCore"));
        assertEquals(EmulatorCore.MANGOS, EmulatorCore.getByName("Mangos"));
    }

    @Test
    void testGetByName_InvalidOrNull() {
        assertNull(EmulatorCore.getByName("Unknown"));
        assertNull(EmulatorCore.getByName(null));
    }
}
