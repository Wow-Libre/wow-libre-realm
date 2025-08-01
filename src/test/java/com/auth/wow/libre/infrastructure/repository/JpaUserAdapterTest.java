package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.user.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserAdapter jpaRealmConfigAdapter;

    @Test
    void findByUsername_ShouldReturnClient_WhenExists() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = jpaRealmConfigAdapter.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotExists() {
        String username = "unknownUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> result = jpaRealmConfigAdapter.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getClientStatusIsTrue_ShouldReturnClient_WhenActive() {
        UserEntity activeClient = new UserEntity();
        activeClient.setStatus(true);

        when(userRepository.findByStatusIsTrue()).thenReturn(Optional.of(activeClient));

        Optional<UserEntity> result = jpaRealmConfigAdapter.getClientStatusIsTrue();

        assertTrue(result.isPresent());
        assertTrue(result.get().isStatus());
        verify(userRepository, times(1)).findByStatusIsTrue();
    }

    @Test
    void getClientStatusIsTrue_ShouldReturnEmpty_WhenNoActiveClients() {
        when(userRepository.findByStatusIsTrue()).thenReturn(Optional.empty());

        Optional<UserEntity> result = jpaRealmConfigAdapter.getClientStatusIsTrue();

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByStatusIsTrue();
    }

    @Test
    void findByRolName_ShouldReturnClients_WhenExists() {
        String role = "ADMIN";
        UserEntity client1 = new UserEntity();
        UserEntity client2 = new UserEntity();

        when(userRepository.findByRol(role)).thenReturn(List.of(client1, client2));

        List<UserEntity> result = jpaRealmConfigAdapter.findByRolName(role);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByRol(role);
    }

    @Test
    void findByRolName_ShouldReturnEmptyList_WhenNoClients() {
        String role = "UNKNOWN";
        when(userRepository.findByRol(role)).thenReturn(List.of());

        List<UserEntity> result = jpaRealmConfigAdapter.findByRolName(role);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByRol(role);
    }

    @Test
    void save_ShouldSaveClientEntity() {
        UserEntity client = new UserEntity();
        when(userRepository.save(client)).thenReturn(client);
        jpaRealmConfigAdapter.save(client);
        verify(userRepository, times(1)).save(client);
    }
}
