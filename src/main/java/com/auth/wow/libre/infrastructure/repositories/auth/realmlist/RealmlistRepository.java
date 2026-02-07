package com.auth.wow.libre.infrastructure.repositories.auth.realmlist;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface RealmlistRepository extends CrudRepository<RealmlistEntity, Long> {
    @Override
    List<RealmlistEntity> findAll();

    /**
     * Reinos que tienen al menos un usuario vinculado (realm_id en users).
     */
    @Query("SELECT r FROM RealmlistEntity r WHERE EXISTS (SELECT 1 FROM UserEntity u WHERE u.realmId.id = r.id)")
    List<RealmlistEntity> findRealmsWithLinkedUsers();

    /**
     * Reinos que no tienen ningún usuario vinculado.
     */
    @Query("SELECT r FROM RealmlistEntity r WHERE NOT EXISTS (SELECT 1 FROM UserEntity u WHERE u.realmId.id = r.id)")
    List<RealmlistEntity> findRealmsWithNoLinkedUsers();
}
