package com.auth.wow.libre.infrastructure.repositories.characters.item_instance;

import com.auth.wow.libre.domain.ports.out.item_instance.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaItemInstanceAdapter implements DeleteItemInstance, ObtainItemInstance {

    private final ItemInstanceRepository itemInstanceRepository;

    public JpaItemInstanceAdapter(ItemInstanceRepository itemInstanceRepository) {
        this.itemInstanceRepository = itemInstanceRepository;
    }

    @Override
    public void delete(ItemInstanceEntity instanceEntity, String transactionId) {
        itemInstanceRepository.delete(instanceEntity);
    }

    @Override
    public Optional<ItemInstanceEntity> findByGuidAndOwnerGuid(Long guid, Long ownerGuid, String transactionId) {
        return itemInstanceRepository.findByIdAndOwnerGuid(guid, ownerGuid);
    }
}
