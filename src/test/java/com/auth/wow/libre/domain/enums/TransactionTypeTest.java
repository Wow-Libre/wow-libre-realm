package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

    @Test
    void testEnumValues() {
        assertEquals("announcement", TransactionType.ANNOUNCEMENT.getDescription());
        assertEquals(10000000.0, TransactionType.ANNOUNCEMENT.getCost());

        assertEquals("bank", TransactionType.BANK.getDescription());
        assertEquals(0.0, TransactionType.BANK.getCost());

        assertEquals("send_money", TransactionType.SEND_MONEY.getDescription());
        assertEquals(10000.0, TransactionType.SEND_MONEY.getCost());

        assertEquals("send_level", TransactionType.SEND_LEVEL.getDescription());
        assertEquals(50000000.0, TransactionType.SEND_LEVEL.getCost());

        assertEquals("send_items", TransactionType.SEND_ITEMS.getDescription());
        assertEquals(0.0, TransactionType.SEND_ITEMS.getCost());
    }
}
