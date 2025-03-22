package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.client.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaClientAdapterTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private JpaClientAdapter jpaClientAdapter;

    @Test
    void findByUsername_ShouldReturnClient_WhenExists() {
        String username = "testUser";
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setUsername(username);

        when(clientRepository.findByUsername(username)).thenReturn(Optional.of(clientEntity));

        Optional<ClientEntity> result = jpaClientAdapter.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(clientRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotExists() {
        String username = "unknownUser";
        when(clientRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<ClientEntity> result = jpaClientAdapter.findByUsername(username);

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByUsername(username);
    }

    @Test
    void getClientStatusIsTrue_ShouldReturnClient_WhenActive() {
        ClientEntity activeClient = new ClientEntity();
        activeClient.setStatus(true);

        when(clientRepository.findByStatusIsTrue()).thenReturn(Optional.of(activeClient));

        Optional<ClientEntity> result = jpaClientAdapter.getClientStatusIsTrue();

        assertTrue(result.isPresent());
        assertTrue(result.get().isStatus());
        verify(clientRepository, times(1)).findByStatusIsTrue();
    }

    @Test
    void getClientStatusIsTrue_ShouldReturnEmpty_WhenNoActiveClients() {
        when(clientRepository.findByStatusIsTrue()).thenReturn(Optional.empty());

        Optional<ClientEntity> result = jpaClientAdapter.getClientStatusIsTrue();

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByStatusIsTrue();
    }

    @Test
    void findByRolName_ShouldReturnClients_WhenExists() {
        String role = "ADMIN";
        ClientEntity client1 = new ClientEntity();
        ClientEntity client2 = new ClientEntity();

        when(clientRepository.findByRol(role)).thenReturn(List.of(client1, client2));

        List<ClientEntity> result = jpaClientAdapter.findByRolName(role);

        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findByRol(role);
    }

    @Test
    void findByRolName_ShouldReturnEmptyList_WhenNoClients() {
        String role = "UNKNOWN";
        when(clientRepository.findByRol(role)).thenReturn(List.of());

        List<ClientEntity> result = jpaClientAdapter.findByRolName(role);

        assertTrue(result.isEmpty());
        verify(clientRepository, times(1)).findByRol(role);
    }

    @Test
    void save_ShouldSaveClientEntity() {
        ClientEntity client = new ClientEntity();
        when(clientRepository.save(client)).thenReturn(client);
        jpaClientAdapter.save(client);
        verify(clientRepository, times(1)).save(client);
    }
}
