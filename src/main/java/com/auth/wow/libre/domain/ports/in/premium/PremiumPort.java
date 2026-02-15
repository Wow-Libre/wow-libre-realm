package com.auth.wow.libre.domain.ports.in.premium;

public interface PremiumPort {
    boolean isPremium(Long accountId);

    void savePremiumStatus(Long accountId, boolean active);
}
