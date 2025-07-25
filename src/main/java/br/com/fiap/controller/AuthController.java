package br.com.fiap.controller;


import java.util.Objects;

// Removed Swagger imports from here

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
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.interfaces.swagger.AuthApi;
import br.com.fiap.model.User;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
public class AuthController implements AuthApi { 

	@Autowired
	private AuthService authService;

	@Autowired
	private RestaurantService restaurantService;

	
	@Override 
	@PostMapping("/login")
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

		if (user != null && authService.verifyPassword(userAuth.password(), user.getPassword())) {
			checkRestaurantExists(user.getId(), userAuth.restaurantId());
			String token = JwtTokenUtil.createToken(user.getLogin(), userAuth.restaurantId());
			return ResponseEntity.ok(token);
		}

		return ResponseEntity.status(401).body("Credenciais inválidas, não autorizado");
	}

	private void checkRestaurantExists(Long ownerId, Long restaurantId) {
		
		if(Objects.isNull(restaurantId)) return;
		
		restaurantService.findByIdAndRestaurantOwnerId(ownerId, restaurantId);
	}
}