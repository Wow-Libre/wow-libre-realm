package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum EmulatorCore {
    TRINITY_CORE(0, "TrinityCore"),
    AZEROTH_CORE(1, "AzerothCore"),
    MANGOS(2, "Mangos");

    private final int id;
    private final String name;

    EmulatorCore(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EmulatorCore getById(int id) {
        return Arrays.stream(values())
                .filter(core -> core.id == id)
                .findFirst()
                .orElse(null);
    }
}
