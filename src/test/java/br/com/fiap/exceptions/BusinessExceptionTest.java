package br.com.fiap.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BusinessExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Business rule violated";
        BusinessException exception = new BusinessException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Business error with cause";
        Throwable cause = new IllegalArgumentException("Invalid input");
        BusinessException exception = new BusinessException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}