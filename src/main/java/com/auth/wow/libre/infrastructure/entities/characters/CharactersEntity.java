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
    private Long money;
    private Integer skin;
    private Integer face;
    @Column(name = "hairstyle")
    private Integer hairStyle;
    @Column(name = "haircolor")
    private Integer hairColor;
    @Column(name = "facialstyle")
    private Integer facialStyle;
    @Column(name = "bankslots")
    private Integer bankSlots;
    @Column(name = "reststate")
    private Integer restState;
    @Column(name = "playerflags")
    private Integer playerFlags;
    @Column(name = "position_x")
    private Integer positionX;
    @Column(name = "position_y")
    private Integer positionY;
    @Column(name = "position_z")
    private Integer positionZ;
    private Integer map;
    @Column(name = "instance_id")
    private Integer instanceId;
    @Column(name = "instance_mode_mask")
    private Integer instanceModeMask;
    private Integer orientation;
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
    @Column(name = "extra_flags")
    private Integer extraFlags;
    @Column(name = "stable_slots")
    private Integer stableSlots;
    @Column(name = "at_login")
    private Integer atLogin;
    private Integer zone;
    @Column(name = "death_expire_time")
    private Integer deathExpireTime;
    @Column(name = "taxi_path")
    private Integer taxiPath;
    @Column(name = "arenapoints")
    private Integer arenaPoints;
    @Column(name = "totalhonorpoints")
    private Integer totalHonorPoints;
    @Column(name = "todayhonorpoints")
    private Integer todayHonorPoints;
    @Column(name = "yesterdayhonorpoints")
    private Integer yesterdayHonorPoints;
    @Column(name = "totalkills")
    private Integer totalKills;
    @Column(name = "todaykills")
    private Integer todayKills;
    @Column(name = "yesterdaykills")
    private Integer yesterdayKills;
    @Column(name = "chosentitle")
    private Integer chosenTitle;
    @Column(name = "knowncurrencies")
    private Integer knownCurrencies;
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
    @Column(name = "talentgroupscount")
    private Integer talentGroupsCount;
    @Column(name = "activetalentgroup")
    private Integer activeTalentGroup;
    @Column(name = "exploredzones")
    private String exploredZones;
    @Column(name = "equipmentcache")
    private String equipmentCache;
    @Column(name = "ammoid")
    private Integer ammoId;
    @Column(name = "knowntitles")
    private String knownTitles;
    @Column(name = "actionbars")
    private Integer actionBars;
    @Column(name = "grantablelevels")
    private Integer grantableLevels;
    @Column(name = "deleteinfos_account")
    private Integer deleteInfosAccount;
    @Column(name = "deleteinfos_name")
    private String deleteInfosName;
    @Column(name = "deletedate")
    private Integer deleteDate;
}
