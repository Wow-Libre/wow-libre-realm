package com.auth.wow.libre.domain.exception;

import com.auth.wow.libre.domain.model.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {
    @Test
    void testBadRequestExceptionProperties() {
        String message = "Invalid request data";
        String transactionId = "12345";

        BadRequestException exception = new BadRequestException(message, transactionId);

        assertEquals(message, exception.getMessage());
        assertEquals(transactionId, exception.transactionId);
        assertEquals(HttpStatus.BAD_REQUEST, exception.httpStatus);
    }
}
