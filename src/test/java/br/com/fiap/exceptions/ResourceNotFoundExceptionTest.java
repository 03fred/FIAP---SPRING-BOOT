package br.com.fiap.exceptions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void serialVersionUIDShouldBeDefined() {
        assertDoesNotThrow(() -> {
            var field = ResourceNotFoundException.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true); 
            long uid = field.getLong(null); 
            assertEquals(1L, uid);
        });
    }

}
