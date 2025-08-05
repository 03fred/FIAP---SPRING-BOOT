package br.com.fiap.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class ConflictExceptionTest {

    @Test
    void shouldCreateConflictExceptionWithMessage() {
        String errorMessage = "Item already exists";
        ConflictException exception = new ConflictException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldBeAnnotatedWithHttpStatusConflict() {
        ResponseStatus status = ConflictException.class.getAnnotation(ResponseStatus.class);

        assertNotNull(status);
        assertEquals(HttpStatus.CONFLICT, status.value());
    }
}
