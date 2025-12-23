package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
public class CharacterDetailDto {
    public Long id;
    public String name;
    @JsonProperty("race_id")
    public Integer raceId;
    public String raceLogo;
    public String race;
    @JsonProperty("class")
    public String classCharacters;
    @JsonProperty("class_id")
    public Integer classId;
    public String classLogo;
    public Integer gender;
    public Integer level;
    public Integer xp;
    public Double money;
    public Integer logoutTime;
    public Integer totalTime;
    public Integer dream;
    public Integer hunger;
    public Integer thirst;


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
        this.dream = character.dream;
        this.hunger = character.hunger;
        this.thirst = character.thirst;
    }

    public CharacterDetailDto(CharacterDetailDto character) {
        this.id = character.id;
        this.name = character.name;
        this.race = character.race;
        this.raceLogo = character.raceLogo;
        this.raceId = character.raceId;
        this.gender = character.gender;
        this.level = character.level;
        this.xp = character.xp;
        this.money = character.money;
        this.classCharacters = character.classCharacters;
        this.classId = character.classId;
        this.classLogo = character.classLogo;
        this.logoutTime = character.logoutTime;
        this.totalTime = character.totalTime;
        this.dream = character.dream;
        this.hunger = character.hunger;
        this.thirst = character.thirst;
    }

}
