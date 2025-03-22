package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum RolType {
    WOW_LIBRE("WOW_LIBRE"),
    CLIENT("CLIENT"),
    ADMIN("ADMIN");

    private final String name;

    RolType(String name) {
        this.name = name;
    }

    public static RolType getById(String name) {
        return Arrays.stream(values())
                .filter(race -> race.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
