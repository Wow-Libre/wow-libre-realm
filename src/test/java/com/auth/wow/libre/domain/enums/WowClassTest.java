package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class WowClassTest {

    @Test
    void testGetById_ValidValues() {
        assertEquals(WowClass.WARRIOR, WowClass.getById(1));
        assertEquals(WowClass.PALADIN, WowClass.getById(2));
        assertEquals(WowClass.HUNTER, WowClass.getById(3));
        assertEquals(WowClass.ROGUE, WowClass.getById(4));
        assertEquals(WowClass.PRIEST, WowClass.getById(5));
        assertEquals(WowClass.DEATH_KNIGHT, WowClass.getById(6));
        assertEquals(WowClass.SHAMAN, WowClass.getById(7));
        assertEquals(WowClass.MAGE, WowClass.getById(8));
        assertEquals(WowClass.WARLOCK, WowClass.getById(9));
        assertEquals(WowClass.MONK, WowClass.getById(10));
    }

    @Test
    void testGetById_InvalidValue() {
        assertEquals(WowClass.DEFAULT, WowClass.getById(99));
    }

    @Test
    void testGetById_ZeroValue() {
        assertEquals(WowClass.DEFAULT, WowClass.getById(0));
    }
}
