package br.com.fiap.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.interfaces.swagger.UserApi;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
public class UserController implements UserApi { 

	@Autowired
	private UserService userService;
	
	@Override
	@PostMapping
	public ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserDTO userDto) {
		this.userService.save(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Usuário cadastrado com sucesso!"));
	}
	
	@Override 
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDto) {
		userService.update(userDto, id);
		return ResponseEntity.ok(Map.of("mensagem", "Usuário atualizado com sucesso."));
	}

	@Override 
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Long id){
		userService.delete(id);
		return ResponseEntity.ok(Map.of("mensagem", "Usuário excluído com sucesso."));
	}

	@Override 
	@GetMapping
	public ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(Pageable pageable) {
		PaginatedResponseDTO<UserResponseDTO> userPage = userService.getAllUsers(pageable);
		return ResponseEntity.ok(userPage);
	}

	@Override 
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

}