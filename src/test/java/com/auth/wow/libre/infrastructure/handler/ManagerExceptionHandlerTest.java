package com.auth.wow.libre.infrastructure.handler;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.model.shared.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerExceptionHandlerTest {

    private final ManagerExceptionHandler handler = new ManagerExceptionHandler();

    @Test
    void testMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(bindingResult.getFieldErrorCount()).thenReturn(1);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<?> response = handler.methodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Invalid Fields"));
    }


    @Test
    void testMissingServletRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param",
                "String");
        ResponseEntity<GenericResponse<Void>> response = handler.missingServletRequestParameterException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Required request parameter 'param' for method parameter " +
                "type String is not present"));
    }

    @Test
    void testGenericErrorException() {
        GenericErrorException exception = new GenericErrorException("Unauthorized", "generic", HttpStatus.UNAUTHORIZED);
        ResponseEntity<GenericResponse<Void>> response = handler.unauthorizedException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("generic", response.getBody().getMessage());
        assertEquals("Unauthorized", response.getBody().getTransactionId());
        assertEquals(401, response.getBody().getCode());

    }
}
