package com.auth.wow.libre.application;

import com.auth.wow.libre.application.services.realmlist.*;
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
        // Simular respuesta del mock
        when(obtainRealmlist.findByAll()).thenReturn(realmlistMock);

        // Ejecutar el método del servicio
        List<RealmlistEntity> result = realmlistService.findByAll();

        // Validar resultados
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Realm 1", result.get(0).getName());
        assertEquals("Realm 2", result.get(1).getName());

        // Verificar que el mock fue llamado una vez
        verify(obtainRealmlist, times(1)).findByAll();
    }
}
