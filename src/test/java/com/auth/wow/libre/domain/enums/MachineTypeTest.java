package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class MachineTypeTest {

    @Test
    void testFindByNameValidValues() {
        assertThat(MachineType.findByName("Item")).isEqualTo(MachineType.ITEMS);
        assertThat(MachineType.findByName("Level")).isEqualTo(MachineType.LEVEL);
        assertThat(MachineType.findByName("Mount")).isEqualTo(MachineType.MOUNT);
        assertThat(MachineType.findByName("Gold")).isEqualTo(MachineType.GOLD);
    }

    @Test
    void testFindByNameCaseInsensitive() {
        assertThat(MachineType.findByName("item")).isEqualTo(MachineType.ITEMS);
        assertThat(MachineType.findByName("LEVEL")).isEqualTo(MachineType.LEVEL);
        assertThat(MachineType.findByName("MoUnT")).isEqualTo(MachineType.MOUNT);
        assertThat(MachineType.findByName("gold")).isEqualTo(MachineType.GOLD);
    }

    @Test
    void testFindByNameInvalidValue() {
        assertThat(MachineType.findByName("INVALID")).isNull();
        assertThat(MachineType.findByName("123")).isNull();
        assertThat(MachineType.findByName("")).isNull();
        assertThat(MachineType.findByName(null)).isNull();
    }
}
