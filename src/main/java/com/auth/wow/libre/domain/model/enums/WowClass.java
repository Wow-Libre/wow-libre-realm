package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
@AllArgsConstructor
public enum  WowClass {
    WARRIOR(1, "Warrior", "https://wow.zamimg.com/images/wow/icons/large/classicon_warrior.jpg"),
    PALADIN(2, "Paladin", "https://wow.zamimg.com/images/wow/icons/large/classicon_paladin.jpg"),
    HUNTER(3, "Hunter", "https://wow.zamimg.com/images/wow/icons/large/classicon_hunter.jpg"),
    ROGUE(4, "Rogue", "https://wow.zamimg.com/images/wow/icons/large/classicon_rogue.jpg"),
    PRIEST(5, "Priest", "https://wow.zamimg.com/images/wow/icons/large/classicon_priest.jpg"),
    DEATH_KNIGHT(6, "Death Knight", "https://wow.zamimg.com/images/wow/icons/large/spell_deathknight_classicon.jpg"),
    SHAMAN(7, "Shaman", "https://wow.zamimg.com/images/wow/icons/large/classicon_shaman.jpg"),
    MAGE(8, "Mage", "https://wow.zamimg.com/images/wow/icons/large/classicon_mage.jpg"),
    WARLOCK(9, "Warlock", "https://wow.zamimg.com/images/wow/icons/large/classicon_warlock.jpg"),
    MONK(10, "Monk", "https://wow.zamimg.com/images/wow/icons/large/classicon_monk.jpg"),
    DEFAULT(0, "", "https://via.placeholder.com/50");

    private final int id;
    private final String className;
    private final String logo;


    public static WowClass getById(int id) {
        return Arrays.stream(values())
                .filter(wowClass -> wowClass.getId() == id)
                .findFirst()
                .orElse(DEFAULT);
    }
}
