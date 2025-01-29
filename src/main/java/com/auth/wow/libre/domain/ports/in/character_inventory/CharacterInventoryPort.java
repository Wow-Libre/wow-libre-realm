package com.auth.wow.libre.domain.ports.in.character_inventory;

import com.auth.wow.libre.domain.model.*;

import java.util.*;

public interface CharacterInventoryPort {

    List<CharacterInventoryModel> findByAllInventory(Long characterId, String transactionId);

    void delete(Long characterId, Long itemId, String transactionId);

    Optional<CharacterInventoryModel> findByGuidAndItem(Long characterId, Long itemId, String transactionId);
}
