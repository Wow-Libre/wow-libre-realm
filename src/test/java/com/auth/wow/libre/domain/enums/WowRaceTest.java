package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class WowRaceTest {
    @Test
    void testGetById_ValidId() {
        assertEquals(WowRace.HUMAN, WowRace.getById(1));
        assertEquals(WowRace.ORC, WowRace.getById(2));
        assertEquals(WowRace.DWARF, WowRace.getById(3));
    }

    @Test
    void testGetById_InvalidId() {
        assertEquals(WowRace.DEFAULT, WowRace.getById(-1));
        assertEquals(WowRace.DEFAULT, WowRace.getById(99));
    }

    @Test
    void testGetById_BoundaryValues() {
        assertEquals(WowRace.DEFAULT, WowRace.getById(0));
        assertEquals(WowRace.MECHAGNOME, WowRace.getById(37));
    }

    @Test
    void testRaceAttributes() {
        WowRace race = WowRace.HUMAN;
        assertEquals(1, race.getId());
        assertEquals("Human", race.getRaceName());
        assertEquals("Alliance", race.getFaction());
        assertNotNull(race.getFemaleIcon());
        assertNotNull(race.getMaleIcon());
    }
}
