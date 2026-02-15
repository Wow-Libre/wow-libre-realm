package com.auth.wow.libre.domain.ports.out.premium;

public interface SavePremium {
    void save(Long accountId, boolean active);
}
