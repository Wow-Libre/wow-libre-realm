package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum StoreItems {
    BANANA("001", 8, 60L),
    MANA("002", 10, 40L),
    BEER("003", 15, 90L),
    CAKE("004", 8, 50L),
    HOTEL("005", 100, 100L);

    private final String code;
    private final Integer multiplier;
    private final Long costGold;

    StoreItems(String code, int multiplier, Long costGold) {
        this.code = code;
        this.multiplier = multiplier;
        this.costGold = costGold;
    }

    public static StoreItems findByCode(String code) {
        return Arrays.stream(StoreItems.values())
                .filter(data -> code != null && data.code.equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(null);
    }
}
