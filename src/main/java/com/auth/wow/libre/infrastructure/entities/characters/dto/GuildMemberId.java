package com.auth.wow.libre.infrastructure.entities.characters.dto;

import jakarta.persistence.*;

import java.io.*;
import java.util.*;


public class GuildMemberId implements Serializable {
    @Id
    @Column(name = "guildid")
    private Long id;
    @Id
    private Long guid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildMemberId that = (GuildMemberId) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(guid, that.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guid);
    }
}
