package com.auth.wow.libre.domain.model;

import lombok.*;

@AllArgsConstructor
@Getter
public class CharacterInventoryModel {
    private Long characterId;
    private Long bag;
    private Long slot;
    private Long item;
    private Long itemId;
    private Long instanceId;
}
