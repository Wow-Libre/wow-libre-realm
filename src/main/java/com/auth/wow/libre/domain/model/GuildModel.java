package com.auth.wow.libre.domain.model;

import java.util.*;

public class GuildModel {
    public final Long id;
    public final String name;
    public final String leaderName;

    public final Long emblemStyle;
    public final Long emblemColor;
    public final Long borderStyle;
    public final Long borderColor;
    public final String info;
    public final String motd;
    public final Date createDate;

    public final Long bankMoney;

    public final Long members;
    public final boolean publicAccess;
    public final String discord;
    public final Boolean multiFaction;

    public GuildModel(Long id, String name, String leaderName, Long emblemStyle, Long emblemColor,
                      Long borderStyle, Long borderColor, String info, String motd, Date createDate, Long bankMoney,
                      Long members, Boolean publicAccess, String discord, Boolean multiFaction) {
        this.id = id;
        this.name = name;
        this.leaderName = leaderName;
        this.emblemStyle = emblemStyle;
        this.emblemColor = emblemColor;
        this.borderStyle = borderStyle;
        this.borderColor = borderColor;
        this.info = info;
        this.motd = motd;
        this.createDate = createDate;
        this.bankMoney = bankMoney;
        this.members = members;
        this.publicAccess = publicAccess != null && publicAccess;
        this.discord = discord;
        this.multiFaction = multiFaction != null && multiFaction;
    }
}
