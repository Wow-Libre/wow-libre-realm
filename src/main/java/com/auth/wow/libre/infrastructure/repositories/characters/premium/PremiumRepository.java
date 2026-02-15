package com.auth.wow.libre.infrastructure.repositories.characters.premium;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface PremiumRepository extends JpaRepository<PremiumEntity, Long> {
    Optional<PremiumEntity> findByAccountId(Long accountId);
}
