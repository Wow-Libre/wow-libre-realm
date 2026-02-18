package com.auth.wow.libre.domain.model;

import com.auth.wow.libre.domain.model.dto.*;

public class CharacterSocialDetail extends CharacterDetailDto {
    public final String flags;
    public final String note;

    public CharacterSocialDetail(CharacterDetailDto character, CharacterSocial characterSocial) {
        super(character);
        this.note = characterSocial.note;
        this.flags = characterSocial.flags == 1 ? "Friend" : "Ignore";
    }
}
