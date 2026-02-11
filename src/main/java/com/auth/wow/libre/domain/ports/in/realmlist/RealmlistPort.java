package com.auth.wow.libre.domain.ports.in.realmlist;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;

import java.util.*;

public interface RealmlistPort {
    List<RealmlistDto> findByAll();

    /**
     * Reinos que tienen al menos un usuario vinculado.
     */
    List<RealmlistDto> findByAllLinked();

    /**
     * Reinos que no tienen ningún usuario vinculado.
     */
    List<RealmlistDto> findByAllNotLinked();

    Optional<RealmlistEntity> findById(Long id);

}
