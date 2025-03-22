package com.auth.wow.libre.domain.exception;

import com.auth.wow.libre.domain.model.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;


class NotFoundExceptionTest {

    @Test
    void testNotFoundExceptionProperties() {
        String message = "Resource not found";
        String transactionId = "trx-001";

        NotFoundException exception = new NotFoundException(message, transactionId);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.transactionId).isEqualTo(transactionId);
        assertThat(exception.code).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exception.httpStatus).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
