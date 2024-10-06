package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;
import com.fasterxml.jackson.annotation.*;

public class CharacterDetailDto {
    protected Long id;
    protected String name;
    @JsonProperty("race_id")
    protected Integer raceId;
    protected String raceLogo;
    protected String race;
    @JsonProperty("class")
    protected String classCharacters;
    @JsonProperty("class_id")
    protected Integer classId;
    protected String classLogo;
    protected Integer gender;
    protected Integer level;
    protected Integer xp;
    protected Long money;
    protected Integer logoutTime;
    protected Integer totalTime;


    public CharacterDetailDto(CharacterModel character) {
        this.id = character.guid;
        this.name = character.name;
        this.race = character.raceName;
        this.raceLogo = character.raceLogo;
        this.raceId = character.raceId;
        this.gender = character.gender;
        this.level = character.level;
        this.xp = character.xp;
        this.money = character.money;
        this.classCharacters = character.className;
        this.classId = character.classCharacters;
        this.classLogo = character.classLogo;
        this.logoutTime = character.logoutTime;
        this.totalTime = character.totalTime;
    }


}
