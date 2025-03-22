package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.server_publications.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JpaServerPublicationsAdapterTest {

    @Mock
    private ServerPublicationsRepository serverPublicationsRepository;

    @InjectMocks
    private JpaServerPublicationsAdapter jpaServerPublicationsAdapter;

    @Test
    void findAll_ShouldReturnServerPublicationsEntities_WhenExists() {
        ServerPublicationsEntity pub1 = new ServerPublicationsEntity();
        ServerPublicationsEntity pub2 = new ServerPublicationsEntity();

        when(serverPublicationsRepository.findAll()).thenReturn(List.of(pub1, pub2));

        List<ServerPublicationsEntity> result = jpaServerPublicationsAdapter.findAll();

        assertEquals(2, result.size());
        verify(serverPublicationsRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoPublicationsExist() {
        when(serverPublicationsRepository.findAll()).thenReturn(List.of());

        List<ServerPublicationsEntity> result = jpaServerPublicationsAdapter.findAll();

        assertTrue(result.isEmpty());
        verify(serverPublicationsRepository, times(1)).findAll();
    }
}
