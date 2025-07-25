package br.com.fiap.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Opcional: Você pode usar @ResponseStatus para definir o status HTTP padrão
// Mas lidar com ele no ControllerExceptionHandler dá mais controle
@ResponseStatus(HttpStatus.UNAUTHORIZED) // Exemplo de uso opcional
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}