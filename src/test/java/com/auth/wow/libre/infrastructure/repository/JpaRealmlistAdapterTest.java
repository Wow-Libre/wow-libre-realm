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

    @Test
    void findByAllLinked_ShouldReturnRealmsWithLinkedUsers() {
        RealmlistEntity realm1 = new RealmlistEntity();
        realm1.setId(1L);
        realm1.setName("Linked Realm");
        when(realmlistRepository.findRealmsWithLinkedUsers()).thenReturn(List.of(realm1));

        List<RealmlistEntity> result = jpaRealmlistAdapter.findByAllLinked();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Linked Realm", result.get(0).getName());
        verify(realmlistRepository, times(1)).findRealmsWithLinkedUsers();
    }

    @Test
    void findByAllNotLinked_ShouldReturnRealmsWithNoLinkedUsers() {
        RealmlistEntity realm1 = new RealmlistEntity();
        realm1.setId(2L);
        realm1.setName("Not Linked Realm");
        when(realmlistRepository.findRealmsWithNoLinkedUsers()).thenReturn(List.of(realm1));

        List<RealmlistEntity> result = jpaRealmlistAdapter.findByAllNotLinked();

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals("Not Linked Realm", result.get(0).getName());
        verify(realmlistRepository, times(1)).findRealmsWithNoLinkedUsers();
    }

    @Test
    void finById_ShouldReturnOptionalWithEntity_WhenExists() {
        RealmlistEntity realm = new RealmlistEntity();
        realm.setId(1L);
        when(realmlistRepository.findById(1L)).thenReturn(Optional.of(realm));

        Optional<RealmlistEntity> result = jpaRealmlistAdapter.finById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(realmlistRepository, times(1)).findById(1L);
    }

    @Test
    void finById_ShouldReturnEmpty_WhenNotExists() {
        when(realmlistRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<RealmlistEntity> result = jpaRealmlistAdapter.finById(999L);

        assertTrue(result.isEmpty());
        verify(realmlistRepository, times(1)).findById(999L);
    }
}
