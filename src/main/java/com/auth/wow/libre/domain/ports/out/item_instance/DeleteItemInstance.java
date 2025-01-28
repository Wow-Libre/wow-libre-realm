package com.auth.wow.libre.domain.ports.out.item_instance;

import com.auth.wow.libre.infrastructure.entities.characters.*;

public interface DeleteItemInstance {
    public void delete(ItemInstanceEntity instanceEntity, String transactionId);
}
