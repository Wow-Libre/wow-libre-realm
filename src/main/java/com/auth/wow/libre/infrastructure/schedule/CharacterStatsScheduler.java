package com.auth.wow.libre.infrastructure.schedule;

import com.auth.wow.libre.domain.ports.out.characters.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

/**
 * Scheduler que reduce periódicamente los stats (hunger, thirst, dream) de los
 * personajes offline
 */
@Component
public class CharacterStatsScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterStatsScheduler.class);

    private final ObtainCharacters obtainCharacters;
    private final SaveCharacters saveCharacters;

    // Cantidad a reducir en cada ejecución (configurable)
    private static final int HUNGER_REDUCTION = 4;
    private static final int THIRST_REDUCTION = 2;
    private static final int DREAM_REDUCTION = 1;

    public CharacterStatsScheduler(ObtainCharacters obtainCharacters, SaveCharacters saveCharacters) {
        this.obtainCharacters = obtainCharacters;
        this.saveCharacters = saveCharacters;
    }

    @Scheduled(cron = "0 0 */4 * * *")
    @Transactional
    public void reduceCharacterStats() {
        String transactionId = "STATS_REDUCTION_SCHEDULER";

        try {
            List<CharactersEntity> offlineCharacters = obtainCharacters.findAllOfflineCharacters(transactionId);

            if (offlineCharacters.isEmpty()) {
                LOGGER.debug("[CharacterStatsScheduler] No offline characters found to update stats");
                return;
            }

            int updatedCount = 0;
            int hungerReduced = 0;
            int thirstReduced = 0;
            int dreamReduced = 0;

            for (CharactersEntity character : offlineCharacters) {
                boolean updated = false;

                // Reducir hambre (no puede bajar de 0)
                Integer currentHunger = Optional.ofNullable(character.getHunger()).orElse(0);
                if (currentHunger > 0) {
                    int newHunger = Math.max(0, currentHunger - HUNGER_REDUCTION);
                    character.setHunger(newHunger);
                    if (newHunger < currentHunger) {
                        hungerReduced++;
                        updated = true;
                    }
                }

                // Reducir sed (no puede bajar de 0)
                Integer currentThirst = Optional.ofNullable(character.getThirst()).orElse(0);
                if (currentThirst > 0) {
                    int newThirst = Math.max(0, currentThirst - THIRST_REDUCTION);
                    character.setThirst(newThirst);
                    if (newThirst < currentThirst) {
                        thirstReduced++;
                        updated = true;
                    }
                }

                // Reducir sueño (no puede bajar de 0, se reduce más lento)
                Integer currentDream = Optional.ofNullable(character.getDream()).orElse(0);
                if (currentDream > 0) {
                    int newDream = Math.max(0, currentDream - DREAM_REDUCTION);
                    character.setDream(newDream);
                    if (newDream < currentDream) {
                        dreamReduced++;
                        updated = true;
                    }
                }

                if (updated) {
                    saveCharacters.save(character, transactionId);
                    updatedCount++;
                }
            }

            LOGGER.info("[CharacterStatsScheduler] Stats reduction completed - Characters updated: {}, " +
                    "Hunger reduced: {}, Thirst reduced: {}, Dream reduced: {}",
                    updatedCount, hungerReduced, thirstReduced, dreamReduced);

        } catch (Exception e) {
            LOGGER.error("[CharacterStatsScheduler] Error reducing character stats - TransactionId: {} - Error: {}",
                    transactionId, e.getMessage(), e);
        }
    }
}
