package com.auth.wow.libre.infrastructure.entities.characters.dto;

import jakarta.persistence.*;

import java.io.*;
import java.util.*;

public class CharacterSocialId implements Serializable {
    @Id
    private Long guid;
    @Id
    private Long friend;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterSocialId that = (CharacterSocialId) o;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(friend, that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, friend);
    }
}
