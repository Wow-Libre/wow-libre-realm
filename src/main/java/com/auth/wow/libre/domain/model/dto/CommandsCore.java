package com.auth.wow.libre.domain.model.dto;

import java.util.*;

public class CommandsCore {
    private CommandsCore() {

    }

    public static String invite(String playerName, String guildName) {
        return String.format(".guild invite %s \"%s\"", playerName, guildName);
    }

    public static String unInvite(String playerName) {
        return String.format(".guild uninvite %s ", playerName);
    }

    public static String sendItems(String playerName, String subject, String body, List<ItemQuantityDto> items) {
        StringBuilder commandBuilder = new StringBuilder(".send items ");
        commandBuilder.append(playerName).append(" \"").append(subject).append("\" \"").append(body).append("\" ");

        for (ItemQuantityDto item : items) {
            commandBuilder.append(item.getId()).append(":").append(item.getQuantity()).append(" ");
        }

        return commandBuilder.toString().trim();
    }

    public static String sendItem(String playerName, String subject, String body, Long item, Integer quantity) {
        return String.format(".send items %s \"%s\" \"%s\" %s:%s", playerName, subject, body, item, quantity);
    }

    public static String sendMail(String playerName, String subject, String body) {
        return String.format(".send mail %s \"%s\" \"%s\"", playerName, subject, body);
    }

    public static String sendMoney(String playerName, String subject, String body, String money) {
        return String.format(".send money %s \"%s\" \"%s\" %s", playerName, subject, body, money);
    }

    public static String characterCustomize(String playerName) {
        return String.format(".character customize %s", playerName);
    }

    public static String characterChangeRace(String playerName) {
        return String.format(".character changerace %s", playerName);
    }

    public static String characterChangeFaction(String playerName) {
        return String.format(".character changefaction %s", playerName);
    }

    public static String sendLevel(String playerName, int level) {
        return String.format(".char level %s %s", playerName, level);
    }

    public static String announcement(String profession, Long level, String characters, String email) {
        return String.format(".name Service: [ %s Lvl: %d - Character: %s  Email: %s ]",
                profession, level, characters, email);
    }

    public static String announcement(String message) {
        return String.format(".name  %s", message);
    }

    public static String kick(String playerName) {
        return String.format(".kick %s", playerName);
    }


}
