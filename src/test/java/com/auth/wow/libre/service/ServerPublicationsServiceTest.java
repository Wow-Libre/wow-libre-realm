package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.server_publications.*;
import com.auth.wow.libre.domain.model.dto.view.*;
import com.auth.wow.libre.domain.ports.out.server_publications.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerPublicationsServiceTest {
    @Mock
    private ObtainServerPublications obtainServerPublications;

    @InjectMocks
    private ServerPublicationsService serverPublicationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublications_ReturnsMappedCards() {
        ServerPublicationsEntity pub1 = new ServerPublicationsEntity();
        pub1.setId(1L);
        pub1.setImg("img1.jpg");
        pub1.setTitle("Title 1");
        pub1.setDescription("Description 1");

        ServerPublicationsEntity pub2 = new ServerPublicationsEntity();
        pub2.setId(2L);
        pub2.setImg("img2.jpg");
        pub2.setTitle("Title 2");
        pub2.setDescription("Description 2");

        when(obtainServerPublications.findAll()).thenReturn(List.of(pub1, pub2));

        List<Card> result = serverPublicationsService.publications();

        assertEquals(2, result.size());
        assertEquals("img1.jpg", result.get(0).getIcon());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Description 1", result.get(0).getDescription());

        assertEquals("img2.jpg", result.get(1).getIcon());
        assertEquals("Title 2", result.get(1).getTitle());
        assertEquals("Description 2", result.get(1).getDescription());

        verify(obtainServerPublications, times(1)).findAll();
    }

    @Test
    void testPublications_EmptyList() {
        when(obtainServerPublications.findAll()).thenReturn(List.of());

        List<Card> result = serverPublicationsService.publications();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(obtainServerPublications, times(1)).findAll();
    }
}
