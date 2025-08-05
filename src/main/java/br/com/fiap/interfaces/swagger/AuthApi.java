package br.com.fiap.interfaces.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fiap.dto.UserAuthorizationDTO;
import br.com.fiap.dto.UserForgotPasswordDTO;
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

    @Operation(summary = "Solicitação de redefinição de senha.",
            description = "Recebe um e-mail ou login e envia um e-mail com as instruções para redefinir a senha.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "E-mail de redefinição enviado com sucesso",
                    content = @Content(schema = @Schema(example = "E-mail enviado com instruções para redefinição"))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/forgot-password")
    ResponseEntity<String> forgotPassword(@Valid @RequestBody UserForgotPasswordDTO userAuth);
    
    @Operation(summary = "Redefinir senha com token.",
            description = "Recebe o token enviado por e-mail e uma nova senha para redefinir o acesso.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token inválido ou senha inválida"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(@RequestParam String token, @Valid @RequestBody UserAuthorizationDTO userAuth);
}
