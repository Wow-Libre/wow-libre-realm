package com.auth.wow.libre.domain.model;

public class CharacterSocial {
    public final Long guid;
    public final Long friend;
    public final Long flags;
    public final String note;

    public CharacterSocial(Long guid, Long friend, Long flags, String note) {
        this.guid = guid;
        this.friend = friend;
        this.flags = flags;
        this.note = note;
    }
}
