package com.auth.wow.libre.domain.model.enums;

import lombok.*;

//.send items Mellyne "" "" 43102:1
@Getter
public enum ItemsMachineType {
    ID_44050("44050", "Caña de pescar de técnica de maestro Kalu'ak", "https://wow.zamimg" +
            ".com/images/wow/icons/large/inv_fishingpole_03.jpg", true),
    ID_46017("46017", "Val'anyr, Martillo de los antiguos reyes", "https://wow.zamimg" +
            ".com/images/wow/icons/large/inv_mace_99.jpg", true),
    ID_49912("49912", "Perky Pug", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_bone_01.jpg", false),
    ID_44834("44834", "Wild Turkey", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_food_15.jpg", false),
    ID_43102("43102", "Frozen Orb", "https://wow.zamimg.com/images/wow/icons/large/spell_frost_frozencore.jpg", false),
    ID_43154("43154", "Tabard of the Argent Crusade", "https://wow.zamimg" +
            ".com/images/wow/icons/large/inv_shirt_guildtabard_01.jpg", true),
    ID_46007("46007", "Bag of Fishing Treasures", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_bag_07_blue" +
            ".jpg", true),
    ID_37182("37182", "Helmet of the Constructor", "https://wow.zamimg.com/images/wow/icons/large/inv_helmet_130.jpg"
            , true),
    ID_44501("44501", "Goblin-machined Piston", "https://wow.zamimg.com/images/wow/icons/large/inv_rod_platinum.jpg",
            false),
    ID_49623("49623", "Agonía de Sombras", "https://wow.zamimg.com/images/wow/icons/large/inv_axe_113.jpg", true),
    ID_40211("40211", "Potion of Speed", "https://wow.zamimg.com/images/wow/icons/large/inv_alchemy_elixir_04.jpg", true),
    ID_44935("44935", "Ring of the Kirin Tor", "https://wow.zamimg.com/images/wow/icons/large/inv_jewelry_ring_74.jpg", true),
    ID_49426("49426", "Emblem of Frost", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_frostemblem_01.jpg", false),
    ID_41599("41599", "Frostweave Bag", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_bag_enchantedmageweave.jpg", true),
    ID_36919("36919", "Cardinal Ruby", "https://wow.zamimg.com/images/wow/icons/large/inv_jewelcrafting_gem_32.jpg", true),
    ID_47241("47241", "Emblem of Triumph", "https://wow.zamimg.com/images/wow/icons/large/spell_holy_summonchampion.jpg", false),
    ID_21177("21177", "Symbol of Kings", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_symbolofkings_01.jpg", false),
    ID_36912("36912", "Saronite Ore", "https://wow.zamimg.com/images/wow/icons/large/inv_ore_saronite_01.jpg", false),
    ID_4614("4614", "Pendant of Myzrael", "https://wow.zamimg.com/images/wow/icons/large/inv_jewelry_necklace_07.jpg", false),
    ID_43499("43499", "Iron Boot Flask", "https://wow.zamimg.com/images/wow/icons/large/inv_drink_01.jpg", false),
    ID_44819("44819", "Baby Blizzard Bear", "https://wow.zamimg.com/images/wow/icons/large/inv_pet_babyblizzardbear.jpg", true),
    ID_19902("19902", "Swift Zulian Tiger", "https://wow.zamimg.com/images/wow/icons/large/ability_mount_jungletiger.jpg", true),
    ID_19019("19019", "Thunderfury, Blessed Blade of the Windseeker", "https://wow.zamimg.com/images/wow/icons/large/inv_sword_39.jpg", true),
    ID_34334("34334", "Thori'dal, the Stars' Fury", "https://wow.zamimg.com/images/wow/icons/large/inv_weapon_bow_39.jpg", true),
    ID_47673("47673", "Sigil of Virulence", "https://wow.zamimg.com/images/wow/icons/large/inv_shield_56.jpg", true),
    ID_50317("50317", "Papa's New Bag", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_bag_26_spellfire.jpg", false),
    ID_44069("44069", "Arcanum of Triumph", "https://wow.zamimg.com/images/wow/icons/large/ability_warrior_shieldmastery.jpg", true),
    ID_46800("46800", "Pilgrim's Attire", "https://wow.zamimg.com/images/wow/icons/large/inv_shirt_black_01.jpg", true),
    ID_12207("12207", "Giant Egg", "https://wow.zamimg.com/images/wow/icons/large/inv_egg_03.jpg", false),
    ID_32837("32837", "Warglaive of Azzinoth", "https://wow.zamimg.com/images/wow/icons/large/inv_weapon_glave_01.jpg", true),
    ID_50734("50734", "Royal Scepter of Terenas II", "https://wow.zamimg.com/images/wow/icons/large/inv_mace_115.jpg", true),
    ID_13086("13086", "Reins of the Winterspring Frostsaber", "https://wow.zamimg.com/images/wow/icons/large/ability_mount_pinktiger.jpg", true),
    ID_43478("43478", "Gigantic Feast", "https://wow.zamimg.com/images/wow/icons/large/ability_hunter_pet_boar.jpg", true),
    ID_33225("33225", "Reins of the Swift Spectral Tiger", "https://wow.zamimg.com/images/wow/icons/large/ability_mount_spectraltiger.jpg", true),
    ID_46036("46036", "Void Sabre", "https://wow.zamimg.com/images/wow/icons/large/inv_sword_133.jpg", true),
    ID_46820("46820", "Vermizo fulgurante", "https://wow.zamimg.com/images/wow/icons/large/inv_elemental_primal_mana.jpg", true),
    ID_22780("22780", "Huevo de múrloc blanco", "https://wow.zamimg.com/images/wow/icons/large/inv_egg_03.jpg", true),
    ID_33079("33079", "Murloc Costume", "https://wow.zamimg.com/images/wow/icons/large/inv_misc_head_murloc_01.jpg", true),
    ID_44738("44738", "Kirin Tor Familiar", "https://wow.zamimg.com/images/wow/icons/large/ability_creature_disease_05.jpg", true);

    private final String code;
    private final String name;
    private final String logo;
    private final boolean limit;

    ItemsMachineType(String code, String name, String logo, boolean limit) {
        this.code = code;
        this.name = name;
        this.logo = logo;
        this.limit = limit;
    }

}
