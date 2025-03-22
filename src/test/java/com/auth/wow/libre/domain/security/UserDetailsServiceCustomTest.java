package com.auth.wow.libre.domain.security;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.model.security.*;
import com.auth.wow.libre.domain.ports.out.client.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceCustomTest {

    @Mock
    private ObtainClient obtainClient;

    @InjectMocks
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        ClientEntity mockClient = new ClientEntity();
        mockClient.setUsername("testUser");
        mockClient.setPassword("testPass");
        mockClient.setRol("ROLE_USER");
        mockClient.setStatus(true);
        mockClient.setId(1L);

        when(obtainClient.findByUsername("testUser")).thenReturn(Optional.of(mockClient));

        // Act
        CustomUserDetails userDetails = userDetailsServiceCustom.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(obtainClient.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UnauthorizedException.class, () ->
                userDetailsServiceCustom.loadUserByUsername("unknownUser"));

        assertEquals("Unauthorized Username", exception.getMessage());
    }
}
