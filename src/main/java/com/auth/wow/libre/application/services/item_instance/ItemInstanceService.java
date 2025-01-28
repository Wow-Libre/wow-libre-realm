package com.auth.wow.libre.application.services.item_instance;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.item_instance.*;
import com.auth.wow.libre.domain.ports.out.item_instance.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ItemInstanceService implements ItemInstancePort {
    private final DeleteItemInstance deleteItemInstance;
    private final ObtainItemInstance obtainItemInstance;

    public ItemInstanceService(DeleteItemInstance deleteItemInstance, ObtainItemInstance obtainItemInstance) {
        this.deleteItemInstance = deleteItemInstance;
        this.obtainItemInstance = obtainItemInstance;
    }

    @Override
    public void delete(Long guid, Long characterId, String transactionId) {
        findByGuidAndOwnerGuid(guid, characterId, transactionId).ifPresentOrElse(
                item -> deleteItemInstance.delete(item, transactionId),
                () -> {
                    throw new InternalException("ItemInstance not found for GUID: " + guid, transactionId);
                });
    }

    @Override
    public Optional<ItemInstanceEntity> findByGuidAndOwnerGuid(Long guid, Long characterId, String transactionId) {
        return obtainItemInstance.findByGuidAndOwnerGuid(guid, characterId, transactionId);
    }
}
