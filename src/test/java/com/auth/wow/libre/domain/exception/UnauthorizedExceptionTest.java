package com.auth.wow.libre.domain.exception;

import com.auth.wow.libre.domain.model.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

class UnauthorizedExceptionTest {

    @Test
    void testUnauthorizedExceptionProperties() {
        String message = "Unauthorized access";
        String transactionId = "trx-002";

        UnauthorizedException exception = new UnauthorizedException(message, transactionId);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.transactionId).isEqualTo(transactionId);
        assertThat(exception.code).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(exception.httpStatus).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
