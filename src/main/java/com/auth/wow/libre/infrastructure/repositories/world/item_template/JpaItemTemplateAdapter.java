package com.auth.wow.libre.infrastructure.repositories.world.item_template;

import com.auth.wow.libre.domain.ports.out.item_template.*;
import com.auth.wow.libre.infrastructure.entities.world.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaItemTemplateAdapter implements ObtainItemTemplate {
    private final ItemTemplateRepository itemTemplateRepository;

    public JpaItemTemplateAdapter(ItemTemplateRepository itemTemplateRepository) {
        this.itemTemplateRepository = itemTemplateRepository;
    }

    @Override
    public Optional<ItemTemplateEntity> findByEntry(Long entry) {
        return itemTemplateRepository.findByEntry(entry);
    }
}
