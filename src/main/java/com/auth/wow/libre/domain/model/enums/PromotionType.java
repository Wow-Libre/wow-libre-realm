package com.auth.wow.libre.domain.model.enums;

import java.util.*;

public enum PromotionType {
    ITEM,
    LEVEL,
    MONEY;

    public static PromotionType findByName(String name) {
        return Arrays.stream(values())
                .filter(promoType -> promoType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
