package com.auth.wow.libre.domain.ports.out.item_template;

import com.auth.wow.libre.infrastructure.entities.world.*;

import java.util.*;

public interface ObtainItemTemplate {
    Optional<ItemTemplateEntity> findByEntry(Long entry);

    Optional<ItemTemplateEntity> findRandomEntry();

}
