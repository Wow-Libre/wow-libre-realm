package com.auth.wow.libre.domain.ports.out.item_instance;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainItemInstance {
    Optional<ItemInstanceEntity> findByGuidAndOwnerGuid(Long guid, Long ownerGuid, String transactionId);
}
