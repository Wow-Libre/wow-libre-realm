package com.auth.wow.libre.application;

import com.auth.wow.libre.domain.context.RequestContext;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RequestContext")
class RequestContextTest {

    @AfterEach
    void tearDown() {
        ThreadContext.remove(HEADER_EMULATOR);
        ThreadContext.remove(HEADER_REALM_ID);
        ThreadContext.remove(HEADER_EXPANSION_ID);
        ThreadContext.remove(CONSTANT_UNIQUE_ID);
    }

    @Test
    void getCurrentEmulator_ReturnsValue_WhenSetInThreadContext() {
        ThreadContext.put(HEADER_EMULATOR, "AZEROTH_CORE");

        assertEquals("AZEROTH_CORE", RequestContext.getCurrentEmulator());
    }

    @Test
    void getCurrentEmulator_ReturnsNull_WhenNotSet() {
        assertNull(RequestContext.getCurrentEmulator());
    }

    @Test
    void getCurrentRealmId_ReturnsValue_WhenSetInThreadContext() {
        ThreadContext.put(HEADER_REALM_ID, "1");

        assertEquals("1", RequestContext.getCurrentRealmId());
    }

    @Test
    void getCurrentRealmId_ReturnsNull_WhenNotSet() {
        assertNull(RequestContext.getCurrentRealmId());
    }

    @Test
    void getCurrentExpansionId_ReturnsValue_WhenSetInThreadContext() {
        ThreadContext.put(HEADER_EXPANSION_ID, "2");

        assertEquals("2", RequestContext.getCurrentExpansionId());
    }

    @Test
    void getCurrentTransactionId_ReturnsValue_WhenSetInThreadContext() {
        ThreadContext.put(CONSTANT_UNIQUE_ID, "tx-123");

        assertEquals("tx-123", RequestContext.getCurrentTransactionId());
    }

    @Test
    void getCurrentTransactionId_ReturnsNull_WhenNotSet() {
        assertNull(RequestContext.getCurrentTransactionId());
    }
}
