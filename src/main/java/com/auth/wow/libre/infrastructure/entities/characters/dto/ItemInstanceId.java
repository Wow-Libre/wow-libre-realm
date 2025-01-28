package com.auth.wow.libre.infrastructure.entities.characters.dto;

import java.io.*;
import java.util.*;

public class ItemInstanceId implements Serializable {
    private Long guid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemInstanceId that = (ItemInstanceId) o;
        return Objects.equals(guid, that.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid);
    }
}
