package com.auth.wow.libre.infrastructure.entities.characters;

import com.auth.wow.libre.infrastructure.entities.characters.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@IdClass(CharacterId.class)
@Data
@Entity
@Table(name = "characters")
public class CharactersEntity implements Serializable {

    @Id
    private Long guid;
    @Id
    private Long account;
    private String name;
    private Integer race;
    @Column(name = "class")
    private Integer classCharacters;
    private Integer gender;
    private Integer level;
    private Integer xp;
    private Double money;
    @Column(name = "bankslots")
    private Integer bankSlots;
    @Column(name = "position_x")
    private Double positionX;
    @Column(name = "position_y")
    private Double positionY;
    @Column(name = "position_z")
    private Double positionZ;
    private Integer map;
    @Column(name = "instance_id")
    private Integer instanceId;
    private Double orientation;
    private String taximask;
    private Integer online;
    private Integer cinematic;
    @Column(name = "totaltime")
    private Integer totalTime;
    @Column(name = "leveltime")
    private Integer levelTime;
    @Column(name = "logout_time")
    private Integer logoutTime;
    @Column(name = "is_logout_resting")
    private Integer isLogoutResting;
    @Column(name = "rest_bonus")
    private Integer restBonus;
    @Column(name = "resettalents_cost")
    private Integer resetTalentsCost;
    @Column(name = "resettalents_time")
    private Integer resetTalentsTime;
    @Column(name = "trans_x")
    private Integer transX;
    @Column(name = "trans_y")
    private Integer transY;
    @Column(name = "trans_z")
    private Integer transZ;
    @Column(name = "trans_o")
    private Integer transO;
    @Column(name = "transguid")
    private Integer transGuid;
    @Column(name = "at_login")
    private Integer atLogin;
    @Column(name = "death_expire_time")
    private Integer deathExpireTime;
    @Column(name = "totalkills")
    private Integer totalKills;
    @Column(name = "todaykills")
    private Integer todayKills;
    @Column(name = "yesterdaykills")
    private Integer yesterdayKills;
    @Column(name = "chosentitle")
    private Integer chosenTitle;
    @Column(name = "watchedfaction")
    private Long watchedFaction;
    private Integer drunk;
    private Integer health;
    private Integer power1;
    private Integer power2;
    private Integer power3;
    private Integer power4;
    private Integer power5;
    private Integer power6;
    private Integer power7;
    private Integer latency;
    @Column(name = "activetalentgroup")
    private Integer activeTalentGroup;
    @Column(name = "exploredzones")
    private String exploredZones;
    @Column(name = "equipmentcache")
    private String equipmentCache;
    @Column(name = "knowntitles")
    private String knownTitles;
    @Column(name = "actionbars")
    private Integer actionBars;
    @Column(name = "deleteinfos_account")
    private Integer deleteInfosAccount;
    @Column(name = "deleteinfos_name")
    private String deleteInfosName;
    @Column(name = "deletedate")
    private Integer deleteDate;
    private Integer zone;
    private Integer dream;
    private Integer hunger;
    private Integer thirst;
}
