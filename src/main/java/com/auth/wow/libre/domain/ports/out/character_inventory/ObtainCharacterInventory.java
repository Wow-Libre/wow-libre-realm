package com.auth.wow.libre.domain.ports.out.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainCharacterInventory {

    Optional<CharacterInventoryModel> findByGuidModel(Long characterId, Long itemId, String transactionId);
    Optional<CharacterInventoryEntity> findByGuid(Long characterId, Long itemId, String transactionId);

    List<CharacterInventoryModel> findByAllInventory(Long characterId, String transactionId);
}
