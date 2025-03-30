package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.google.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.infrastructure.client.*;
import com.auth.wow.libre.infrastructure.client.dto.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoogleServiceTest {

    @Mock
    private GoogleClient googleClient;

    @InjectMocks
    private GoogleService googleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyRecaptcha_Success() {
        String token = "valid-token";
        String ip = "127.0.0.1";
        VerifyCaptchaResponse mockResponse = new VerifyCaptchaResponse(true, "example.com");

        when(googleClient.verifyRecaptcha(any(VerifyCaptchaRequest.class))).thenReturn(mockResponse);

        VerifyCaptchaResponse response = googleService.verifyRecaptcha(token, ip);

        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertEquals("example.com", response.getHostname());
        verify(googleClient, times(1)).verifyRecaptcha(any(VerifyCaptchaRequest.class));
    }

    @Test
    void testVerifyRecaptcha_NullToken_ThrowsException() {
        Exception exception = assertThrows(InternalException.class, () ->
                googleService.verifyRecaptcha(null, "127.0.0.1")
        );
        assertEquals("The recaptcha fields are null so it is not possible to validate the code",
                exception.getMessage());
    }

    @Test
    void testVerifyRecaptcha_NullIpAddress_ThrowsException() {
        Exception exception = assertThrows(InternalException.class, () ->
                googleService.verifyRecaptcha("valid-token", null)
        );
        assertEquals("The recaptcha fields are null so it is not possible to validate the code",
                exception.getMessage());
    }
}
