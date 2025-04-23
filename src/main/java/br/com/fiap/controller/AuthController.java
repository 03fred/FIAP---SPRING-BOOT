package br.com.fiap.controller;

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
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	

	@PostMapping
	public ResponseEntity<?> authorization(@Valid @RequestBody UserAuthorizationDTO userAuth) {
		
		User user = this.userService.getUserByEmail(userAuth.email());
		if(this.userService.verifyPassword(userAuth.password(), user.getPassword())) {
	       String token  = JwtTokenUtil.createToken();
	       return ResponseEntity.ok(token);
		}
	   
		return ResponseEntity.status(401).body("Não autorizado");
	}
}
