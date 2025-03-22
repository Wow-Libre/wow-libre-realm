package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class BenefitTypeTest {
    @Test
    void findByName_ShouldReturnEnum_WhenValidNameProvided() {
        assertThat(BenefitType.findByName("ITEM")).isEqualTo(BenefitType.ITEM);
        assertThat(BenefitType.findByName("customize")).isEqualTo(BenefitType.CUSTOMIZE);
        assertThat(BenefitType.findByName("Change_Faction")).isEqualTo(BenefitType.CHANGE_FACTION);
    }

    @Test
    void findByName_ShouldBeCaseInsensitive() {
        assertThat(BenefitType.findByName("item")).isEqualTo(BenefitType.ITEM);
        assertThat(BenefitType.findByName("CuSToMizE")).isEqualTo(BenefitType.CUSTOMIZE);
    }

    @Test
    void findByName_ShouldTrimSpaces() {
        assertThat(BenefitType.findByName("  ITEM  ")).isEqualTo(BenefitType.ITEM);
        assertThat(BenefitType.findByName("\tcustomize\n")).isEqualTo(BenefitType.CUSTOMIZE);
    }

    @Test
    void findByName_ShouldReturnNull_WhenInvalidNameProvided() {
        assertThat(BenefitType.findByName("INVALID_NAME")).isNull();
        assertThat(BenefitType.findByName("changefaction")).isNull(); // Debe ser con espacio o guion bajo
    }

    @Test
    void findByName_ShouldReturnNull_WhenNullOrEmptyProvided() {
        assertThat(BenefitType.findByName(null)).isNull();
        assertThat(BenefitType.findByName("")).isNull();
        assertThat(BenefitType.findByName("   ")).isNull();
    }
}
