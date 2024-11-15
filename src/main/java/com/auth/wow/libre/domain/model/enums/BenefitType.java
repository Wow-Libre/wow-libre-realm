package com.auth.wow.libre.domain.model.enums;

public enum BenefitType {
    ITEM,
    CUSTOMIZE,
    CHANGE_FACTION,
    CHANGE_RACE,
    MONEY;

    public static BenefitType findByName(String name) {
        if (name == null) {
            return null;
        }
        return BenefitType.valueOf(name.toUpperCase());
    }
}
