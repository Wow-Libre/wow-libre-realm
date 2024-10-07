package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;

import java.time.*;

public record AccountDetailDto(
        Long id,
        String username,
        String email,
        String expansion,
        boolean online,
        String failedLogins,
        LocalDate joinDate,
        String lastIp,
        String muteReason,
        String muteBy,
        boolean mute,
        LocalDate lastLogin,
        String os,
        AccountBanned accountBanned
) {



}
