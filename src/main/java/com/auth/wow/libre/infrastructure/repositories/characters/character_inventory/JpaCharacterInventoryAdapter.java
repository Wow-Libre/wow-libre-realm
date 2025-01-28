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
    public Optional<CharacterInventoryEntity> findByGuid(Long guid, Long item, String transactionId) {
        return characterInventoryRepository.findByGuidAndItem(guid, item);
    }

    @Override
    public List<CharacterInventoryModel> findByGuid(Long guid, String transactionId) {
        return  null;
    }
}
