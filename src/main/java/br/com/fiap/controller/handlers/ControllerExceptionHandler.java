package br.com.fiap.controller.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.fiap.dto.HttpMessageNotReadableExceptionDTO;
import br.com.fiap.dto.ResourceNotFoundDTO;
import br.com.fiap.dto.ValidationErrorDto;
import br.com.fiap.services.ResourceNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResourceNotFoundDTO> handlerResourceNotFoundException(ResourceNotFoundException e){
		var statusCode = HttpStatus.NOT_FOUND;
		return ResponseEntity.status(statusCode.value()).body(new ResourceNotFoundDTO(e.getMessage(), statusCode.value()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorDto> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
		var statusCode = HttpStatus.BAD_REQUEST;
		List<String> errors = new ArrayList<>();
		
		for(var erro : e.getBindingResult().getFieldErrors()) {
			errors.add(erro.getField() + " : " + erro.getDefaultMessage());
		}
		return ResponseEntity.status(statusCode.value()).body(new ValidationErrorDto(errors, statusCode.value()));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<HttpMessageNotReadableExceptionDTO> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e){
		 String mensagem = "Erro ao ler a requisição JSON. Verifique o formato dos dados.";
	        if (e.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
	            com.fasterxml.jackson.databind.exc.InvalidFormatException ife = 
	            		(com.fasterxml.jackson.databind.exc.InvalidFormatException) e.getCause();
	            if (ife.getTargetType().isAssignableFrom(java.util.Date.class) ||
	                ife.getTargetType().isAssignableFrom(java.time.LocalDate.class) ||
	                ife.getTargetType().isAssignableFrom(java.time.LocalDateTime.class)) {
	                mensagem = String.format("Erro de formato na data '%s'. O formato esperado é: ANO-MES-DIA", ife.getValue());
	            }
	        }
	        
		 var statusCode = HttpStatus.BAD_REQUEST;

		return ResponseEntity.status(statusCode.value()).body(new HttpMessageNotReadableExceptionDTO(mensagem, statusCode.value()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleUniqueConstraintViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		logger.error(ex.getMessage());
		Map<String, Object> body = new HashMap<>();
		body.put("message", "O valor fornecido já existe."); // Mensagem genérica
		body.put("status", HttpStatus.CONFLICT.value());
		body.put("error", HttpStatus.CONFLICT.getReasonPhrase());
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
}
