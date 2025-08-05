package br.com.fiap.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UnauthorizedExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Unauthorized access";
        UnauthorizedException exception = new UnauthorizedException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Unauthorized with cause";
        Throwable cause = new IllegalStateException("Cause exception");
        UnauthorizedException exception = new UnauthorizedException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}