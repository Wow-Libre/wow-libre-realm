package com.auth.wow.libre.domain.ports.out.character_inventory;

import com.auth.wow.libre.infrastructure.entities.characters.*;

public interface DeleteCharacterInventory {
    void delete(CharacterInventoryEntity characterInventoryEntity, String transactionId);
}
