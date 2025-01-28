package com.auth.wow.libre.infrastructure.repositories.characters.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface CharacterInventoryRepository extends CrudRepository<CharacterInventoryEntity, Long> {
    Optional<CharacterInventoryEntity> findByGuidAndItem(Long guid, Long item);

    @Query("SELECT new com.auth.wow.libre.domain.model.CharacterInventoryModel( " +
            "chi.guid, chi.bag, chi.slot, chi.item, it.itemEntry, it.guid) " +
            "FROM CharacterInventoryEntity chi " +
            "INNER JOIN ItemInstanceEntity it ON chi.item = it.guid " +
            "WHERE chi.guid = :guid")
    List<CharacterInventoryModel> findByAllInventory(@Param("guid") Long guid);
}
