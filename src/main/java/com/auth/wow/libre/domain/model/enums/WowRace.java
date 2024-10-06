package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public enum WowRace {
    DEFAULT(0, "", "", "", ""),
    HUMAN(1, "Human", "Alliance", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_human_female" +
                  ".jpg", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_human_male.jpg"),
    ORC(2, "Orc", "Horde", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_orc_female_brn.jpg",
                "https://wow.zamimg.com/images/wow/icons/large/achievement_character_orc_male.jpg"),
    DWARF(3, "Dwarf", "Alliance", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_dwarf_female" +
                  ".jpg", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_dwarf_male.jpg"),
    NIGHT_ELF(4, "Night Elf", "Alliance", "", ""),
    UNDEAD(5, "Undead", "Horde", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_undead_female" +
                   ".jpg", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_undead_male.jpg"),
    TAUREN(6, "Tauren", "Horde", "https://wow.zamimg.com/images/wow/icons/large/race_tauren_female.jpg", "https://wow" +
                   ".zamimg.com/images/wow/icons/large/race_tauren_male.jpg"),
    GNOME(7, "Gnome", "Alliance", "https://wow.zamimg.com/images/wow/icons/large/inv_gnometoy.jpg", "https://wow" +
                  ".zamimg.com/images/wow/icons/large/achievement_character_gnome_male.jpg"),
    TROLL(8, "Troll", "Horde", "https://wow.zamimg.com/images/wow/icons/large/achievement_character_troll_female.jpg"
                  , "https://wow.zamimg.com/images/wow/icons/large/achievement_character_troll_male" +
                  ".jpg"),
    GOBLIN(9, "Goblin", "Horde", "https://wow.zamimg.com/images/wow/icons/large/achievement_femalegoblinhead.jpg",
                   "https://wow.zamimg.com/images/wow/icons/large/achievement_goblinhead.jpg"),
    BLOOD_ELF(10, "Blood Elf", "Horde", "https://wow.zamimg" +
                      ".com/images/wow/icons/large/achievement_character_bloodelf_female.jpg", "https://wow.zamimg" +
                      ".com/images/wow/icons/large/achievement_character_bloodelf_male.jpg"),
    DRAENEI(11, "Draenei", "Alliance", "https://wow.zamimg" +
                    ".com/images/wow/icons/large/achievement_character_draenei_female.jpg", "https://wow.zamimg" +
                    ".com/images/wow/icons/large/achievement_character_draenei_male.jpg"),
    WORGEN(22, "Worgen", "Alliance", "https://wow.zamimg.com/images/wow/icons/large/race_worgen_female.jpg", "https" +
                   "://wow.zamimg.com/images/wow/icons/large/race_worgen_male.jpg"),
    PANDAREN_ALLIANCE(24, "Pandaren", "Alliance", "", ""),
    PANDAREN_HORDE(25, "Pandaren", "Horde", "", ""),
    NIGHTBORNE(27, "Nightborne", "Horde", "https://wow.zamimg.com/images/wow/icons/large/inv_nightbornefemale.jpg",
                       "https://wow.zamimg.com/images/wow/icons/large/inv_nightbornemale.jpg"),
    HIGHMOUNTAIN_TAUREN(28, "Highmountain Tauren", "Horde", "https://wow.zamimg" +
                                ".com/images/wow/icons/large/race_highmountaintauren_female.jpg", "https://wow.zamimg" +
                                ".com/images/wow/icons/large/race_highmountaintauren_male.jpg"),
    VOID_ELF(29, "Void Elf", "Alliance", "https://wow.zamimg.com/images/wow/icons/large/race_voidelf_female.jpg",
                     "https://wow.zamimg.com/images/wow/icons/large/race_voidelf_male.jpg"),
    LIGHTFORGED_DRAENEI(30, "Lightforged Draenei", "Alliance", "", ""),
    DARK_IRON_DWARF(34, "Dark Iron Dwarf", "Alliance", "", ""),
    VULPERA(35, "Vulpera", "Horde", "", ""),
    MAGHAR_ORC(36, "Mag'har Orc", "Horde", "", ""),
    MECHAGNOME(37, "Mechagnome", "Alliance", "", "");

    private final int id;
    private final String raceName;
    private final String faction;
    private final String femaleIcon;
    private final String maleIcon;


    public static WowRace getById(int id) {
        return Arrays.stream(values())
                .filter(race -> race.getId() == id)
                .findFirst()
                .orElse(DEFAULT);
    }
}
