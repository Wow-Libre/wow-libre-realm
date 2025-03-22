package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MountTypeTest {
    @Test
    void testEnumValuesAreNotNull() {
        for (MountType mount : MountType.values()) {
            assertNotNull(mount.getCode(), "El código del mount no debería ser nulo");
            assertNotNull(mount.getName(), "El nombre del mount no debería ser nulo");
            assertNotNull(mount.getLogo(), "El logo del mount no debería ser nulo");
        }
    }

    @Test
    void testEnumValueByCode() {
        MountType mount = MountType.ID_50818;
        assertEquals("50818", mount.getCode());
        assertEquals("Invincible's Reins", mount.getName());
        assertEquals("https://wow.zamimg.com/images/wow/icons/large/spell_deathknight_summondeathcharger.jpg",
                mount.getLogo());
    }

    @Test
    void testEnumContainsSpecificMount() {
        assertDoesNotThrow(() -> MountType.valueOf("ID_50818"));
        assertDoesNotThrow(() -> MountType.valueOf("ID_52200"));
        assertDoesNotThrow(() -> MountType.valueOf("ID_44413"));
    }

    @Test
    void testInvalidEnumValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> MountType.valueOf("INVALID_ID"));
    }
}
