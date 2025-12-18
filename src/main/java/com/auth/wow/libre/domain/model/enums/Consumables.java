package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum Consumables {
    DREAM,
    THIRST,
    HUNGER;

    public static Consumables findByName(String name) {
        return Arrays.stream(Consumables.values())
                .filter(data -> name != null && data.name().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }
}
