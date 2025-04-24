package com.auth.wow.libre.domain.strategy.commands;

import com.auth.wow.libre.domain.model.enums.*;

import static com.auth.wow.libre.domain.model.enums.EmulatorCore.AZEROTH_CORE;
import static com.auth.wow.libre.domain.model.enums.EmulatorCore.TRINITY_CORE;

public class CommandStrategyFactory {
    public static CommandStrategy getStrategy(Expansion expansion, String core) {
        if (core.equalsIgnoreCase(TRINITY_CORE.getName())) {
            if (expansion == Expansion.WAR_WITHIN) return new TrinityWarWithinCommandStrategy();
        } else if (core.equalsIgnoreCase(AZEROTH_CORE.getName())) {
            if (expansion == Expansion.WRATH_OF_THE_LICH_KING) return new AzerothLkCommandStrategy();
        }
        throw new UnsupportedOperationException("No strategy for this combination");
    }
}
