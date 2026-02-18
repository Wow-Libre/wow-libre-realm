package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
public class CharacterDetailDto {
    public final Long id;
    public final String name;
    @JsonProperty("race_id")
    public final Integer raceId;
    public final String raceLogo;
    public final String race;
    @JsonProperty("class")
    public final String classCharacters;
    @JsonProperty("class_id")
    public final Integer classId;
    public final String classLogo;
    public final Integer gender;
    public final Integer level;
    public final Integer xp;
    public final Double money;
    public final Integer logoutTime;
    public final Integer totalTime;
    public final Integer dream;
    public final Integer hunger;
    public final Integer thirst;


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
