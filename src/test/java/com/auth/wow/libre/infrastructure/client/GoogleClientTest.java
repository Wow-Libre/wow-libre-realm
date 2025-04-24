package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.infrastructure.client.dto.*;
import com.auth.wow.libre.infrastructure.conf.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;
import org.springframework.web.client.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configurations configurations;

    @InjectMocks
    private GoogleClient googleClient;

    private VerifyCaptchaRequest request;
    private String apiUrl;

    @BeforeEach
    void setUp() {
        request = new VerifyCaptchaRequest("test-response", "localhost");
        apiUrl = "https://www.google.com/recaptcha/api/siteverify";
    }

    @Test
    void testVerifyRecaptcha_Success() {
        when(configurations.getGoogleApiSecret()).thenReturn("secret-key");
        VerifyCaptchaResponse mockResponse = new VerifyCaptchaResponse(true, "localhost");
        ResponseEntity<VerifyCaptchaResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(VerifyCaptchaResponse.class)))
                .thenReturn(responseEntity);

        VerifyCaptchaResponse response = googleClient.verifyRecaptcha(request);

        assertNotNull(response);
        assertTrue(response.getSuccess());
    }

    @Test
    void testVerifyRecaptcha_ClientError() {
        when(configurations.getGoogleApiSecret()).thenReturn("secret-key");
        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(VerifyCaptchaResponse.class)))
                .thenThrow(new InternalException("Transaction failed due to client or server error", ""));

        assertThrows(InternalException.class, () -> googleClient.verifyRecaptcha(request));
    }

    @Test
    void testVerifyRecaptcha_UnexpectedError() {
        when(configurations.getGoogleApiSecret()).thenReturn("secret-key");
        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(VerifyCaptchaResponse.class)))
                .thenThrow(new RuntimeException("Unexpected Error"));

        assertThrows(InternalException.class, () -> googleClient.verifyRecaptcha(request));
    }
}
