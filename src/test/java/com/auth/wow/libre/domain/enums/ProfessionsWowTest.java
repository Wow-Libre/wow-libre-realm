package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class ProfessionsWowTest {
    @Test
    void getById_ShouldReturnCorrectProfession() {
        assertThat(ProfessionsWow.getById(171)).isEqualTo(ProfessionsWow.ALCHEMY);
        assertThat(ProfessionsWow.getById(164)).isEqualTo(ProfessionsWow.BLACKSMITH);
        assertThat(ProfessionsWow.getById(333)).isEqualTo(ProfessionsWow.ENCHANTMENT);
        assertThat(ProfessionsWow.getById(202)).isEqualTo(ProfessionsWow.ENGINEERING);
        assertThat(ProfessionsWow.getById(182)).isEqualTo(ProfessionsWow.HERBALISM);
        assertThat(ProfessionsWow.getById(773)).isEqualTo(ProfessionsWow.INSCRIPTION);
        assertThat(ProfessionsWow.getById(755)).isEqualTo(ProfessionsWow.JEWELCRAFTING);
        assertThat(ProfessionsWow.getById(165)).isEqualTo(ProfessionsWow.LEATHERWORKING);
        assertThat(ProfessionsWow.getById(186)).isEqualTo(ProfessionsWow.MINING);
        assertThat(ProfessionsWow.getById(393)).isEqualTo(ProfessionsWow.SKINNING);
        assertThat(ProfessionsWow.getById(197)).isEqualTo(ProfessionsWow.TAILORING);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdNotFound() {
        assertThat(ProfessionsWow.getById(99)).isNull();
        assertThat(ProfessionsWow.getById(-1)).isNull();
    }

    @Test
    void professions_ShouldHaveCorrectAttributes() {
        ProfessionsWow profession = ProfessionsWow.ENCHANTMENT;
        assertThat(profession.getId()).isEqualTo(333);
        assertThat(profession.getDescription()).isEqualTo("enchantment");
        assertThat(profession.getLogo()).isEqualTo("https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcQFVAm9CJqhqvJDBT-SQpqwhEpyifJbMtStrQ&s");
    }
}
