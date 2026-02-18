package com.auth.wow.libre.domain.model.enums;

import lombok.*;


@Getter
public enum GmCommand {
    WOW_CLASSIC(0, "World of Warcraft Clásico", "account create %s %s", "account set password %s %s"),
    THE_BURNING_CRUSADE(1, "The Burning Crusade", "account create %s %s", "account set password"),
    WRATH_OF_THE_LICH_KING(2, "Wrath of the Lich King", "account create %s %s", "account set password"),
    CATACLYSM(3, "Cataclysm", "bnetaccount create %s %s", "account set password"),
    MISTS_OF_PANDARIA(4, "Mists of Pandaria", "bnetaccount create %s %s", "account set password"),
    WARLORDS_OF_DRAENOR(5, "Warlords of Draenor", "bnetaccount create %s %s", "account set password"),
    LEGION(6, "Legion", "bnetaccount create %s %s", "account set password"),
    BATTLE_FOR_AZEROTH(7, "Battle for Azeroth", "bnetaccount create %s %s", "account set password"),
    SHADOWLANDS(8, "Shadowlands", "bnetaccount create %s %s", "account set password"),
    DRAGON_FLIGHT(9, "Dragonflight", "bnetaccount create %s %s", "account set password"),
    WAR_WITHING(10, "War Within", "bnetaccount create %s %s", "account set password");

    private final int expansionId;
    private final String expansionName;
    private final String commandCreate;
    private final String commandChangePassword;

    GmCommand(int expansionId, String expansionName, String commandCreate, String commandChangePassword) {
        this.expansionId = expansionId;
        this.expansionName = expansionName;
        this.commandCreate = commandCreate;
        this.commandChangePassword = commandChangePassword;
    }


}
