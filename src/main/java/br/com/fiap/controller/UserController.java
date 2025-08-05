package br.com.fiap.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.PasswordUpdateDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserPartialUpdateDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.interfaces.swagger.UserApi;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
public class UserController implements UserApi { 

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	        
	@Override
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserDTO userDto) {
		this.userService.save(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Usuário cadastrado com sucesso!"));
	}
	
	@Override 
	@PreAuthorize("hasRole('ADMIN')")		
	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
		userService.update(userUpdateDTO, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override 
	@PreAuthorize("hasRole('USER')")
	@PutMapping
	public ResponseEntity<Map<String, String>> update(@Valid @RequestBody UserDTO userDto) {
		userService.update(userDto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@Override 
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Long id){
		userService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override 
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(Pageable pageable) {
		PaginatedResponseDTO<UserResponseDTO> userPage = userService.getAllUsers(pageable);
		return ResponseEntity.ok(userPage);
	}

	@Override 
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/detail/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/update-partial/{id}")
	public ResponseEntity<Map<String, String>> updatePartial(
			@PathVariable Long id,
			@RequestBody UserPartialUpdateDTO dto) {

		userService.updatePartial(id, dto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/update-password/{id}")
	public ResponseEntity<Map<String, String>> updatePassword(
			@PathVariable Long id,
			@Valid @RequestBody PasswordUpdateDTO dto) {
		userService.updatePassword(id, dto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}