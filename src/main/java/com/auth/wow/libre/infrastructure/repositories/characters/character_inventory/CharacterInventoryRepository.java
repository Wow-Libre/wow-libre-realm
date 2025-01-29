package com.auth.wow.libre.infrastructure.repositories.characters.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface CharacterInventoryRepository extends CrudRepository<CharacterInventoryEntity, Long> {
    @Query("SELECT new com.auth.wow.libre.domain.model.CharacterInventoryModel( " +
            "chi.guid, it.count, chi.slot, chi.item, it.itemEntry, it.id) " +
            "FROM CharacterInventoryEntity chi " +
            "INNER JOIN ItemInstanceEntity it ON chi.item = it.id " +
            "WHERE chi.guid = :guid AND chi.item = :item")
    Optional<CharacterInventoryModel> findByGuidAndItemModel(@Param("guid") Long guid, @Param("item") Long item);


    Optional<CharacterInventoryEntity> findByGuidAndItem(Long guid, Long item);


    @Query("SELECT new com.auth.wow.libre.domain.model.CharacterInventoryModel( " +
            "chi.guid, it.count, chi.slot, chi.item, it.itemEntry, it.id) " +
            "FROM CharacterInventoryEntity chi " +
            "INNER JOIN ItemInstanceEntity it ON chi.item = it.id " +
            "WHERE chi.guid = :guid")
    List<CharacterInventoryModel> findByAllInventory(@Param("guid") Long guid);
}
