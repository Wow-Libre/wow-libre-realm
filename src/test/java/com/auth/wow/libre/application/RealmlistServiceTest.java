package com.auth.wow.libre.application;

import com.auth.wow.libre.application.services.realmlist.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.ports.out.realmlist.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealmlistServiceTest {

    @Mock
    private ObtainRealmlist obtainRealmlist;

    @InjectMocks
    private RealmlistService realmlistService;

    private List<RealmlistEntity> realmlistMock;

    @BeforeEach
    void setUp() {
        RealmlistEntity realm1 = new RealmlistEntity();
        realm1.setId(1L);
        realm1.setName("Realm 1");
        realm1.setAddress("192.168.1.1");

        RealmlistEntity realm2 = new RealmlistEntity();
        realm2.setId(2L);
        realm2.setName("Realm 2");
        realm2.setAddress("192.168.1.2");

        realmlistMock = List.of(realm1, realm2);
    }

    @Test
    void testFindByAll_Success() {
        when(obtainRealmlist.findByAll()).thenReturn(realmlistMock);

        List<RealmlistDto> result = realmlistService.findByAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Realm 1", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("Realm 2", result.get(1).name());
        verify(obtainRealmlist, times(1)).findByAll();
    }

    @Test
    void testFindByAllLinked_Success() {
        when(obtainRealmlist.findByAllLinked()).thenReturn(realmlistMock);

        List<RealmlistDto> result = realmlistService.findByAllLinked();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Realm 1", result.get(0).name());
        verify(obtainRealmlist, times(1)).findByAllLinked();
    }

    @Test
    void testFindByAllNotLinked_Success() {
        List<RealmlistEntity> oneRealm = List.of(realmlistMock.get(0));
        when(obtainRealmlist.findByAllNotLinked()).thenReturn(oneRealm);

        List<RealmlistDto> result = realmlistService.findByAllNotLinked();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Realm 1", result.get(0).name());
        verify(obtainRealmlist, times(1)).findByAllNotLinked();
    }

    @Test
    void testFindByAll_EmptyList() {
        when(obtainRealmlist.findByAll()).thenReturn(List.of());

        List<RealmlistDto> result = realmlistService.findByAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(obtainRealmlist, times(1)).findByAll();
    }
}
