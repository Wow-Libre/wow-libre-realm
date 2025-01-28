package com.auth.wow.libre.domain.ports.out.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainCharacterInventory {

    Optional<CharacterInventoryEntity> findByGuid(Long characterId, Long item, String transactionId);

    List<CharacterInventoryModel> findByGuid(Long characterId, String transactionId);
}
