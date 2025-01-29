package com.auth.wow.libre.infrastructure.repositories.characters.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.out.character_inventory.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaCharacterInventoryAdapter implements ObtainCharacterInventory, DeleteCharacterInventory {
    private final CharacterInventoryRepository characterInventoryRepository;

    public JpaCharacterInventoryAdapter(CharacterInventoryRepository characterInventoryRepository) {
        this.characterInventoryRepository = characterInventoryRepository;
    }

    @Override
    public void delete(CharacterInventoryEntity characterInventoryEntity, String transactionId) {
        characterInventoryRepository.delete(characterInventoryEntity);
    }

    @Override
    public Optional<CharacterInventoryModel> findByGuidModel(Long guid, Long itemId, String transactionId) {
        return characterInventoryRepository.findByGuidAndItemModel(guid, itemId);
    }

    @Override
    public Optional<CharacterInventoryEntity> findByGuid(Long characterId, Long itemId, String transactionId) {
        return characterInventoryRepository.findByGuidAndItem(characterId, itemId);
    }

    @Override
    public List<CharacterInventoryModel> findByAllInventory(Long guid, String transactionId) {
        return characterInventoryRepository.findByAllInventory(guid);
    }
}
