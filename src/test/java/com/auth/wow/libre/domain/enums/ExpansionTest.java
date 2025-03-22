package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class ExpansionTest {
    @Test
    void getById_ShouldReturnCorrectExpansion() {
        assertThat(Expansion.getById(0)).isEqualTo(Expansion.WOW_CLASSIC);
        assertThat(Expansion.getById(1)).isEqualTo(Expansion.THE_BURNING_CRUSADE);
        assertThat(Expansion.getById(2)).isEqualTo(Expansion.WRATH_OF_THE_LICH_KING);
        assertThat(Expansion.getById(3)).isEqualTo(Expansion.CATACLYSM);
        assertThat(Expansion.getById(4)).isEqualTo(Expansion.MISTS_OF_PANDARIA);
        assertThat(Expansion.getById(5)).isEqualTo(Expansion.WARLORDS_OF_DRAENOR);
        assertThat(Expansion.getById(6)).isEqualTo(Expansion.LEGION);
        assertThat(Expansion.getById(7)).isEqualTo(Expansion.BATTLE_FOR_AZEROTH);
        assertThat(Expansion.getById(8)).isEqualTo(Expansion.SHADOWLANDS);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdNotFound() {
        assertThat(Expansion.getById(99)).isNull(); // ID no válido
        assertThat(Expansion.getById(-1)).isNull(); // ID negativo
    }

    @Test
    void expansion_ShouldHaveCorrectAttributes() {
        Expansion expansion = Expansion.WRATH_OF_THE_LICH_KING;

        assertThat(expansion.getValue()).isEqualTo(2);
        assertThat(expansion.getDisplayName()).isEqualTo("Wrath of the Lich King");
        assertThat(expansion.getLogo()).isEqualTo("https://i.ibb.co/4jg39fc/lichking-wow-libre-expansion.png");
    }
}
