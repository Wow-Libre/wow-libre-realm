package com.auth.wow.libre.application.services.character_inventory;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_inventory.*;
import com.auth.wow.libre.domain.ports.in.item_instance.*;
import com.auth.wow.libre.domain.ports.out.character_inventory.*;
import com.auth.wow.libre.domain.ports.out.item_template.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.entities.world.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CharacterInventoryService implements CharacterInventoryPort {

    private final ItemInstancePort instancePort;
    private final ObtainCharacterInventory obtainCharacterInventory;
    private final ObtainItemTemplate obtainItemTemplate;

    public CharacterInventoryService(ItemInstancePort instancePort, ObtainCharacterInventory obtainCharacterInventory
            , ObtainItemTemplate obtainItemTemplate) {
        this.instancePort = instancePort;
        this.obtainCharacterInventory = obtainCharacterInventory;
        this.obtainItemTemplate = obtainItemTemplate;
    }


    @Override
    public List<CharacterInventoryModel> findByAllInventory(Long characterId, String transactionId) {
        return obtainCharacterInventory.findByAllInventory(characterId, transactionId).stream().map(findName ->
                new CharacterInventoryModel(findName.getCharacterId(), findName.getBag() == 0 ? 1 : findName.getBag()
                        , findName.getSlot(),
                        findName.getItem(), findName.getItemId(), findName.getInstanceId(),
                        obtainItemTemplate.findByEntry(findName.getItemId()).map(ItemTemplateEntity::getName).orElse(
                                ""))
        ).toList();
    }

    @Override
    public void delete(Long characterId, Long itemId, String transactionId) {

        obtainCharacterInventory.findByGuid(characterId, itemId, transactionId).ifPresentOrElse(
                item -> {

                    instancePort.delete(item.getItem(), item.getGuid(), transactionId);
                },
                () -> {
                    throw new InternalException("ItemInstance not found for GUID: ", transactionId);
                });


    }

    @Override
    public Optional<CharacterInventoryModel> findByGuidAndItem(Long characterId, Long itemId, String transactionId) {
        return obtainCharacterInventory.findByGuidModel(characterId, itemId, transactionId)
                .map(inventory -> {

                    Optional<ItemInstanceEntity> itemInstance =
                            instancePort.findByGuidAndOwnerGuid(inventory.getItem(), inventory.getCharacterId(),
                                    transactionId);

                    if (itemInstance.isEmpty()) {
                        throw new InternalException("It was not possible to find the item associated with the " +
                                "character", transactionId);
                    }
                    return inventory;
                });
    }


}
