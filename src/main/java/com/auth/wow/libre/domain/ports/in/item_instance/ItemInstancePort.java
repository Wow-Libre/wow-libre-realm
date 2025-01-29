package com.auth.wow.libre.domain.ports.in.item_instance;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ItemInstancePort {
    void delete(Long guid, Long ownerGuid, String transactionId);

    Optional<ItemInstanceEntity> findByGuidAndOwnerGuid(Long guid, Long ownerGuid, String transactionId);

}
