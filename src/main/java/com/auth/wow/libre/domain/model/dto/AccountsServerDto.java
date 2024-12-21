package com.auth.wow.libre.domain.model.dto;

import java.time.*;

public record AccountsServerDto(
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
        String os
) {



}
