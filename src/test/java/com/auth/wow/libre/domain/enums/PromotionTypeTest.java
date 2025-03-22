package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PromotionTypeTest {
    @Test
    void testFindByNameValidValues() {
        assertEquals(PromotionType.ITEM, PromotionType.findByName("ITEM"));
        assertEquals(PromotionType.LEVEL, PromotionType.findByName("LEVEL"));
        assertEquals(PromotionType.MONEY, PromotionType.findByName("MONEY"));
    }

    @Test
    void testFindByNameCaseInsensitive() {
        assertEquals(PromotionType.ITEM, PromotionType.findByName("item"));
        assertEquals(PromotionType.LEVEL, PromotionType.findByName("Level"));
        assertEquals(PromotionType.MONEY, PromotionType.findByName("mOnEy"));
    }

    @Test
    void testFindByNameInvalidValue() {
        assertNull(PromotionType.findByName("INVALID"));
        assertNull(PromotionType.findByName("123"));
        assertNull(PromotionType.findByName(""));
        assertNull(PromotionType.findByName(null));
    }
}
