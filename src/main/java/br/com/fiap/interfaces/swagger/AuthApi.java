package br.com.fiap.interfaces.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.UserAuthorizationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Autenticação")
public interface AuthApi {

    @Operation(summary = "Autenticação de usuário.",
            description = "Recebe senha e e-mail ou nome de usuário, a escolha do próprio usuário, e retorna um token JWT caso as credenciais estejam corretas.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado",
                    content = @Content(schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas, não autorizado",
                    content = @Content(schema = @Schema(implementation = String.class, example = "Credenciais inválidas, não autorizado")))
    })
    ResponseEntity<?> authorization(@Valid @RequestBody UserAuthorizationDTO userAuth);
}