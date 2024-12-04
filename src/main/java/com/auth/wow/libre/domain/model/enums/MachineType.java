package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum MachineType {
    ITEMS("Item"),
    LEVEL("Level"),
    MENAS("Menas"),
    GOLD("Gold");

    private final String name;

    MachineType(String name) {
        this.name = name;
    }

    public static MachineType getName(String name) {
        return Arrays.stream(values())
                .filter(data -> Objects.equals(data.name, name))
                .findFirst()
                .orElse(null);
    }
}
