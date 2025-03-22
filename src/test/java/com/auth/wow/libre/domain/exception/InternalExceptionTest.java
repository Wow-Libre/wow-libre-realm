package com.auth.wow.libre.domain.exception;

import com.auth.wow.libre.domain.model.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class InternalExceptionTest {

    @Test
    void testInternalExceptionProperties() {
        String message = "Unexpected error";
        String transactionId = "trx-789";

        InternalException exception = new InternalException(message, transactionId);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.transactionId).isEqualTo(transactionId);
        assertThat(exception.code).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exception.httpStatus).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
