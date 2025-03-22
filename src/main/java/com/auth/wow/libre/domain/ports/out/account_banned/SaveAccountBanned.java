package com.auth.wow.libre.domain.ports.out.account_banned;

public interface SaveAccountBanned {
    void save(Long accountId, Long banDate, Long unBanDate, String bannedBy, String banReason, boolean active);
}
