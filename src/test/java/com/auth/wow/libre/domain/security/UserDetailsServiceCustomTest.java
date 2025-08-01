package com.auth.wow.libre.domain.security;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.model.security.*;
import com.auth.wow.libre.domain.ports.out.user.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceCustomTest {

    @Mock
    private ObtainUser obtainUser;

    @InjectMocks
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        UserEntity mockClient = new UserEntity();
        mockClient.setUsername("testUser");
        mockClient.setPassword("testPass");
        mockClient.setRol("ROLE_USER");
        mockClient.setStatus(true);
        mockClient.setId(1L);

        when(obtainUser.findByUsername("testUser")).thenReturn(Optional.of(mockClient));

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
        when(obtainUser.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UnauthorizedException.class, () ->
                userDetailsServiceCustom.loadUserByUsername("unknownUser"));

        assertEquals("Unauthorized Username", exception.getMessage());
    }
}
