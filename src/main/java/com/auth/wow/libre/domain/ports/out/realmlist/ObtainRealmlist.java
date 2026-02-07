package com.auth.wow.libre.domain.ports.out.realmlist;

import com.auth.wow.libre.infrastructure.entities.auth.*;

import java.util.*;

public interface ObtainRealmlist {

    List<RealmlistEntity> findByAll();

    /**
     * Reinos que tienen al menos un usuario vinculado (tabla users).
     */
    List<RealmlistEntity> findByAllLinked();

    /**
     * Reinos que no tienen ningún usuario vinculado.
     */
    List<RealmlistEntity> findByAllNotLinked();

    Optional<RealmlistEntity> finById(Long id);
}
