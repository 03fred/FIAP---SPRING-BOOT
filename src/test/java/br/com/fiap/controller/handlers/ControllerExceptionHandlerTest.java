package br.com.fiap.controller.handlers;

import br.com.fiap.dto.HttpMessageNotReadableExceptionDTO;
import br.com.fiap.dto.ResourceNotFoundDTO;
import br.com.fiap.dto.ValidationErrorDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new ControllerExceptionHandler();
    }

    @Test
    void testHandlerResourceNotFoundException() {
        String msg = "Recurso não encontrado";
        ResourceNotFoundException ex = new ResourceNotFoundException(msg);

        ResponseEntity<ResourceNotFoundDTO> response = handler.handlerResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(msg, response.getBody().message());
    }

    @Test
    void testHandlerMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        FieldError fieldError1 = new FieldError("obj", "campo1", "não pode ser vazio");
        FieldError fieldError2 = new FieldError("obj", "campo2", "deve ser maior que zero");

        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<ValidationErrorDTO> response = handler.handlerMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        List<String> errors = response.getBody().errors();
        assertTrue(errors.contains("campo1 : não pode ser vazio"));
        assertTrue(errors.contains("campo2 : deve ser maior que zero"));
    }

    @Test
    void testHandlerHttpMessageNotReadableException_GenericMessage() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getCause()).thenReturn(null);

        ResponseEntity<HttpMessageNotReadableExceptionDTO> response = handler.handlerHttpMessageNotReadableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro ao ler a requisição JSON. Verifique o formato dos dados.", response.getBody().message());
    }

    @Test
    void testHandleUniqueConstraintViolation() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Violação de chave única");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleUniqueConstraintViolation(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("O valor fornecido já existe.", body.get("message"));
        assertEquals(HttpStatus.CONFLICT.value(), body.get("status"));
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), body.get("error"));
    }

}