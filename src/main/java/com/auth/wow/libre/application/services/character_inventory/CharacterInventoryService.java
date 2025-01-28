package com.auth.wow.libre.application.services.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_inventory.*;
import com.auth.wow.libre.domain.ports.in.item_instance.*;
import com.auth.wow.libre.domain.ports.out.character_inventory.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CharacterInventoryService implements CharacterInventoryPort {

    private final ItemInstancePort instancePort;
    private final DeleteCharacterInventory deleteCharacterInventory;
    private final ObtainCharacterInventory obtainCharacterInventory;

    public CharacterInventoryService(ItemInstancePort instancePort, DeleteCharacterInventory deleteCharacterInventory
            , ObtainCharacterInventory obtainCharacterInventory) {
        this.instancePort = instancePort;
        this.deleteCharacterInventory = deleteCharacterInventory;
        this.obtainCharacterInventory = obtainCharacterInventory;
    }


    @Override
    public List<CharacterInventoryModel> findByInventory(Long characterId, String transactionId) {
        return obtainCharacterInventory.findByGuid(characterId, transactionId);
    }

    @Override
    public void delete(Long characterId, Long itemId, String transactionId) {

        obtainCharacterInventory.findByGuid(characterId, itemId, transactionId).ifPresentOrElse(
                item -> deleteCharacterInventory.delete(item, transactionId),
                () -> {
                    throw new InternalException("ItemInstance not found for GUID: ", transactionId);
                });


      //  instancePort.delete(characterId, transactionId);
    }


}
