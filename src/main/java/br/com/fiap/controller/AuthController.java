package br.com.fiap.controller;


import jakarta.validation.Valid;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.dto.UserAuthorizationDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.services.AuthService;
import br.com.fiap.model.User;


@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
@Tag(name = "Autenticação")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping
	@Operation(summary = "Autenticação de usuário.",
			description = "Recebe senha e e-mail ou nome de usuário, a escolha do próprio usuário, e retorna um token JWT caso as credenciais estejam corretas.",
			method = "POST")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado"),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas, não autorizado")
	})
	public ResponseEntity<?> authorization(@Valid @RequestBody UserAuthorizationDTO userAuth) {
		User user = null;

		try {
			if (userAuth.identificador().contains("@")) {
				user = authService.getUserByEmail(userAuth.identificador());
			} else {
				user = authService.getUserByLogin(userAuth.identificador());
			}
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(401).body("Credenciais inválidas, não autorizado.");
		}

		if (authService.verifyPassword(userAuth.password(), user.getPassword())) {
			String token = JwtTokenUtil.createToken();
			return ResponseEntity.ok(token);
		}

		return ResponseEntity.status(401).body("Credenciais inválidas, não autorizado");
	}

}
