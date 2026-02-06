package com.auth.wow.libre.application.services.realmlist;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.ports.in.realmlist.*;
import com.auth.wow.libre.domain.ports.out.realmlist.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class RealmlistService implements RealmlistPort {
    private final ObtainRealmlist obtainRealmlist;

    public RealmlistService(ObtainRealmlist obtainRealmlist) {
        this.obtainRealmlist = obtainRealmlist;
    }

    @Override
    public List<RealmlistDto> findByAll() {
        return obtainRealmlist.findByAll().stream()
                .map(data -> new RealmlistDto(
                        data.getId(),
                        data.getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RealmlistEntity> findById(Long id) {
        return obtainRealmlist.finById(id);
    }
}
