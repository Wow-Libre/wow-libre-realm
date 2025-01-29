package com.auth.wow.libre.domain.model;

import lombok.*;


@Getter
public class CharacterInventoryModel {
    private final Long characterId;
    private final Long bag;
    private final Long slot;
    private final Long item;
    private final Long itemId;
    private final Long instanceId;
    private String name;

    public CharacterInventoryModel(Long characterId, Long bag, Long slot, Long item, Long itemId, Long instanceId) {
        this.characterId = characterId;
        this.bag = bag;
        this.slot = slot;
        this.item = item;
        this.itemId = itemId;
        this.instanceId = instanceId;
    }

    public CharacterInventoryModel(Long characterId, Long bag, Long slot, Long item, Long itemId, Long instanceId, String name) {
        this.characterId = characterId;
        this.bag = bag;
        this.slot = slot;
        this.item = item;
        this.itemId = itemId;
        this.instanceId = instanceId;
        this.name = name;
    }
}
