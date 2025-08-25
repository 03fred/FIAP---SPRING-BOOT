package br.com.fiap.interfaces.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.application.useCases.users.CreateUserUseCase;
import br.com.fiap.domain.entities.User;
import br.com.fiap.dto.UserDTO;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
public class UserController  { 


    private final CreateUserUseCase createUser;
    private final PasswordEncoder passwordEncoder;
    
    public UserController(CreateUserUseCase createUser, PasswordEncoder passwordEncoder) {
        this.createUser = createUser;
        this.passwordEncoder = passwordEncoder;
    }
    
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserDTO userDto) {
		User user = new User(userDto, this.passwordEncoder.encode(userDto.password()));
		  
		 User saved = createUser.execute(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Usuário cadastrado com sucesso!"));
	}
	
}