package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;

import java.util.*;

public class GuildDto extends GuildModel {
    public Cta cta;
    public String formattedBankMoney;

    public Boolean isLeader;

    public GuildDto(Long id, String name, String leaderName, Long emblemStyle, Long emblemColor, Long borderStyle,
                    Long borderColor, String info, String motd, Date createDate, Long bankMoney, Long members,
                    Cta cta, boolean publicAccess, String formattedBankMoney, Boolean isLeader, String discord,
                    Boolean multiFaction) {
        super(id, name, leaderName, emblemStyle, emblemColor, borderStyle, borderColor, info, motd, createDate,
                bankMoney, members, publicAccess, discord, multiFaction);
        this.cta = cta;
        this.formattedBankMoney = formattedBankMoney;
        this.isLeader = isLeader;
    }


    public static class Cta {
        public String label;
        public boolean linked;

        public Cta(String label, boolean linked) {
            this.label = label;
            this.linked = linked;
        }
    }
}
