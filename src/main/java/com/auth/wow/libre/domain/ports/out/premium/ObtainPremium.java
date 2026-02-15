package com.auth.wow.libre.domain.ports.out.premium;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainPremium {
    Optional<PremiumEntity> isPremium(Long accountId);
}
