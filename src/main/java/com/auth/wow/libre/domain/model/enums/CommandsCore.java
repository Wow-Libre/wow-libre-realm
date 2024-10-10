package com.auth.wow.libre.domain.model.enums;

import com.auth.wow.libre.domain.model.*;

import java.util.*;

public class CommandsCore {
    public static String invite(String playerName, String guildName) {
        return String.format(".guild invite %s \"%s\"", playerName, guildName);
    }

    public static String unInvite(String playerName) {
        return String.format(".guild uninvite %s ", playerName);
    }

    public static String sendItems(String playerName, String subject, String body, List<ItemQuantityModel> items) {
        StringBuilder commandBuilder = new StringBuilder(".send items ");
        commandBuilder.append(playerName).append(" \"").append(subject).append("\" \"").append(body).append("\" ");

        for (ItemQuantityModel item : items) {
            commandBuilder.append(item.id()).append(":").append(item.quantity()).append(" ");
        }

        return commandBuilder.toString().trim();
    }

    public static String sendMail(String playerName, String subject, String body) {
        return String.format(".send mail %s \"%s\" \"%s\"", playerName, subject, body);
    }

    public static String sendMoney(String playerName, String subject, String body, String money) {
        return String.format(".send money %s \"%s\" \"%s\" %s", playerName, subject, body, money);
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
