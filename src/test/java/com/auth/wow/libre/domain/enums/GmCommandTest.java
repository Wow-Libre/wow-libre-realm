package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GmCommandTest {

    @Test
    void testAllEnumValuesPresent() {
        assertEquals(11, GmCommand.values().length);
    }

    @Test
    void testClassicExpansion() {
        GmCommand classic = GmCommand.WOW_CLASSIC;
        assertEquals(0, classic.getExpansionId());
        assertEquals("World of Warcraft Clásico", classic.getExpansionName());
        assertEquals("account create %s %s", classic.getCommandCreate());
        assertEquals("account set password %s %s", classic.getCommandChangePassword());
    }

    @Test
    void testDragonflightExpansion() {
        GmCommand dragonflight = GmCommand.DRAGON_FLIGHT;
        assertEquals(9, dragonflight.getExpansionId());
        assertEquals("Dragonflight", dragonflight.getExpansionName());
        assertEquals("bnetaccount create %s %s", dragonflight.getCommandCreate());
        assertEquals("account set password", dragonflight.getCommandChangePassword());
    }

    @Test
    void testAllCreateCommandsHavePlaceholders() {
        for (GmCommand cmd : GmCommand.values()) {
            assertTrue(cmd.getCommandCreate().contains("%s"),
                    "Expected placeholder in create command: " + cmd.getCommandCreate());
        }
    }

    @Test
    void testAllHaveValidExpansionId() {
        int expectedId = 0;
        for (GmCommand cmd : GmCommand.values()) {
            assertEquals(expectedId++, cmd.getExpansionId());
        }
    }

}
