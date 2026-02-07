package com.auth.wow.libre.domain.security;

import com.auth.wow.libre.domain.model.security.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.core.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private CustomUserDetails userDetails;
    private final Long userId = 1L;
    private final String username = "testUser";
    private final String password = "securePassword";
    private Collection<? extends GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        authorities = List.of(Mockito.mock(GrantedAuthority.class));
        boolean accountNonExpired = true;
        boolean accountNonLocked = true;
        boolean credentialsNonExpired = true;
        boolean enabled = true;
        Long realmId = 1L;
        userDetails = new CustomUserDetails(authorities, password, username, accountNonExpired,
                accountNonLocked, credentialsNonExpired, enabled, userId, realmId);
    }

    @Test
    void testGetId() {
        assertEquals(userId, userDetails.getId());
    }

    @Test
    void testGetUsername() {
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void testGetAuthorities() {
        assertEquals(authorities, userDetails.getAuthorities());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

}
