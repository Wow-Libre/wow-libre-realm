package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "guild")
public class GuildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guildid")
    private Long id;

    private String name;
    @Column(name = "leaderguid")
    private Integer leaderGuid;
    @Column(name = "emblemstyle")
    private Long emblemStyle;
    @Column(name = "emblemcolor")
    private Long emblemColor;
    @Column(name = "borderstyle")
    private Long borderStyle;
    @Column(name = "bordercolor")
    private Long borderColor;
    private String info;
    private String motd;
    @Column(name = "createdate")
    private Long createDate;
    @Column(name = "bankmoney")
    private Long bankMoney;
    @Column(name = "public_access")
    private Boolean publicAccess;
    private String discord;
    @Column(name = "multi_faction")
    private Boolean multiFaction;
}
