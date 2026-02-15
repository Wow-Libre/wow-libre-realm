package com.auth.wow.libre.application.services.premium;

import com.auth.wow.libre.domain.ports.in.premium.*;
import com.auth.wow.libre.domain.ports.out.premium.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

@Service
public class PremiumService implements PremiumPort {
    private final ObtainPremium obtainPremium;
    private final SavePremium savePremium;

    public PremiumService(ObtainPremium obtainPremium, SavePremium savePremium) {
        this.obtainPremium = obtainPremium;
        this.savePremium = savePremium;
    }

    public boolean isPremium(Long accountId) {
        return obtainPremium.isPremium(accountId).map(PremiumEntity::isActive).orElse(false);
    }

    @Override
    public void savePremiumStatus(Long accountId, boolean active) {
        savePremium.save(accountId, active);

    }
}
