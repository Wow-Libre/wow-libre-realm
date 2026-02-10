package com.auth.wow.libre.domain.context;

import org.apache.logging.log4j.ThreadContext;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

/**
 * Contexto de la petición actual. Permite acceder a datos propagados desde el header/JWT
 * (emulator, realmId, expansionId, transactionId) sin pasarlos por cada capa.
 */
public final class RequestContext {

    private RequestContext() {
    }

    public static String getCurrentEmulator() {
        return ThreadContext.get(HEADER_EMULATOR);
    }

    public static String getCurrentRealmId() {
        return ThreadContext.get(HEADER_REALM_ID);
    }

    public static String getCurrentExpansionId() {
        return ThreadContext.get(HEADER_EXPANSION_ID);
    }

    public static String getCurrentTransactionId() {
        return ThreadContext.get(CONSTANT_UNIQUE_ID);
    }
}
