package com.auth.wow.libre.infrastructure.entities.characters.dto;

import java.io.*;
import java.util.*;

public class CharacterId implements Serializable {
    private Long guid;
    private Long account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterId that = (CharacterId) o;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, account);
    }
}
