package com.auth.wow.libre.domain.model.enums;

public enum PromotionType {
    ITEM,
    LEVEL,
    MONEY;

    public static PromotionType findByName(String name) {
        if (name == null) {
            return null;
        }
        return PromotionType.valueOf(name.toUpperCase());
    }
}
