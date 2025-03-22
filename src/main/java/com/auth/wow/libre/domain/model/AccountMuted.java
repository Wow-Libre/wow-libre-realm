package com.auth.wow.libre.domain.model;

import java.time.*;

public record AccountMuted(Long id, LocalDateTime muteDate, LocalDateTime muteTime, String mutedBy, String muteReason) {
}
