package com.auth.wow.libre.domain.ports.in.account_banned;

public interface SaveBannedPort {
    void save(Long accountId, Long banDate, Long unBanDate, String bannedBy, String banReason, boolean active);
}
