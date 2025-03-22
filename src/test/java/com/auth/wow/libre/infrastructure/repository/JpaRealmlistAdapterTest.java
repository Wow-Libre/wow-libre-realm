package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.realmlist.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaRealmlistAdapterTest {

    @Mock
    private RealmlistRepository realmlistRepository;

    @InjectMocks
    private JpaRealmlistAdapter jpaRealmlistAdapter;

    @Test
    void findByAll_ShouldReturnRealmlistEntities_WhenExists() {
        RealmlistEntity realm1 = new RealmlistEntity();
        RealmlistEntity realm2 = new RealmlistEntity();

        when(realmlistRepository.findAll()).thenReturn(List.of(realm1, realm2));

        List<RealmlistEntity> result = jpaRealmlistAdapter.findByAll();

        assertEquals(2, result.size());
        verify(realmlistRepository, times(1)).findAll();
    }

    @Test
    void findByAll_ShouldReturnEmptyList_WhenNoRealmsExist() {
        when(realmlistRepository.findAll()).thenReturn(List.of());

        List<RealmlistEntity> result = jpaRealmlistAdapter.findByAll();

        assertTrue(result.isEmpty());
        verify(realmlistRepository, times(1)).findAll();
    }
}
