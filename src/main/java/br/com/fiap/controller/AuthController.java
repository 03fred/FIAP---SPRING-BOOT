package br.com.fiap.controller;

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
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
@Tag(name = "Autenticação")
public class AuthController {
	
	@Autowired
	private UserService userService;
	

	@PostMapping
	@Operation(summary = "Autenticação de usuário.",
	description = "Recebe e-mail e senha, e retorna um token JWT caso as credenciais estejam corretas.",
	method = "POST")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado"),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas, não autorizado")
	})
	public ResponseEntity<?> authorization(@Valid @RequestBody UserAuthorizationDTO userAuth) {
		
		User user = this.userService.getUserByEmail(userAuth.email());
//		if(this.userService.verifyPassword(userAuth.password(), user.getPassword())) {
//	       String token  = JwtTokenUtil.createToken();
//	       return ResponseEntity.ok(token);
//		}
	   
		return ResponseEntity.status(401).body("Não autorizado");
	}
}
