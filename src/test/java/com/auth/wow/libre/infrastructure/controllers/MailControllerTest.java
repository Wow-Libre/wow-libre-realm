package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.mail.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailControllerTest {

    @Mock
    private MailPort mailPort;

    @InjectMocks
    private MailController mailController;

    private static final Long CHARACTER_ID = 1L;
    private static final String TRANSACTION_ID = "12345";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mails_ShouldReturnMails_WhenCharacterExists() {
        MailsDto mockMails = new MailsDto(new ArrayList<>(), 1);
        when(mailPort.getMails(CHARACTER_ID, TRANSACTION_ID)).thenReturn(mockMails);

        // Act
        ResponseEntity<GenericResponse<MailsDto>> response = mailController.mails(TRANSACTION_ID, CHARACTER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockMails, response.getBody().getData());
        verify(mailPort, times(1)).getMails(CHARACTER_ID, TRANSACTION_ID);
    }

    @Test
    void mails_ShouldReturnEmpty_WhenNoMailsFound() {
        // Arrange
        when(mailPort.getMails(CHARACTER_ID, TRANSACTION_ID)).thenReturn(null);

        // Act
        ResponseEntity<GenericResponse<MailsDto>> response = mailController.mails(TRANSACTION_ID, CHARACTER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(Objects.requireNonNull(response.getBody()).getData());

        verify(mailPort, times(1)).getMails(CHARACTER_ID, TRANSACTION_ID);
    }
}
