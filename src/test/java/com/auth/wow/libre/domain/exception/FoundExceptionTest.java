package com.auth.wow.libre.domain.exception;

import com.auth.wow.libre.domain.model.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

class FoundExceptionTest {

    @Test
    void testFoundExceptionProperties() {
        String message = "Not Found";
        String transactionId = "12345";

        FoundException exception = new FoundException(message, transactionId);

        assertEquals(message, exception.getMessage());
        assertEquals(transactionId, exception.transactionId);
        assertEquals(HttpStatus.CONFLICT, exception.httpStatus);
    }
}
