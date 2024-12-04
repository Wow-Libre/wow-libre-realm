package com.auth.wow.libre.domain.model.enums;

import lombok.*;

@Getter
public enum MineType {
    IRON_ORE("2772", "Iron Ore", "https://wow.zamimg.com/images/wow/icons/large/inv_ore_iron_01.jpg"),
    GOLD_ORE("2776", "Gold Ore", "https://wow.zamimg.com/images/wow/icons/large/inv_ore_gold_01.jpg");

    private final String code;
    private final String name;
    private final String logo;

    MineType(String code, String name, String logo) {
        this.code = code;
        this.name = name;
        this.logo = logo;
    }

}
