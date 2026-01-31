package com.auth.wow.libre.infrastructure.context;

/**
 * Contexto del reino actual por request (ThreadLocal).
 * Usado por los datasources de enrutado para elegir la BD de characters/world.
 */
public final class RealmContext {

    private static final ThreadLocal<Long> CURRENT_REALM_ID = new ThreadLocal<>();

    private RealmContext() {
    }

    public static void setCurrentRealmId(Long realmId) {
        CURRENT_REALM_ID.set(realmId);
    }

    public static Long getCurrentRealmId() {
        return CURRENT_REALM_ID.get();
    }

    public static void clear() {
        CURRENT_REALM_ID.remove();
    }
}
