package com.auth.wow.libre.infrastructure.repositories.characters.premium;

import com.auth.wow.libre.domain.ports.out.premium.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPremiumAdapter implements ObtainPremium, SavePremium {
    private final PremiumRepository premiumRepository;

    public JpaPremiumAdapter(PremiumRepository premiumRepository) {
        this.premiumRepository = premiumRepository;
    }

    @Override
    public Optional<PremiumEntity> isPremium(Long accountId) {
        return premiumRepository.findByAccountId(accountId);
    }

    @Override
    public void save(Long accountId, boolean status) {
        Optional<PremiumEntity> premiumEntity = premiumRepository.findByAccountId(accountId);

        if (premiumEntity.isEmpty()) {
            PremiumEntity premium = new PremiumEntity();
            premium.setAccountId(accountId);
            premium.setActive(status);
            premiumRepository.save(premium);
        } else {
            PremiumEntity premium = premiumEntity.get();
            premium.setActive(status);
            premiumRepository.save(premium);
        }


    }
}
