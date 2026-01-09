package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum BenefitType {
    ITEM,
    CUSTOMIZE,
    CHANGE_FACTION,
    CHANGE_RACE,
    LEVEL,
    MONEY;

    public static BenefitType findByName(String name) {
        return Arrays.stream(BenefitType.values())
                .filter(data -> name != null && data.name().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }
}
