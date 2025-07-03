package br.com.fiap.controller;

import java.util.Map;

import br.com.fiap.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.interfaces.swagger.UserApi;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
public class UserController implements UserApi { 

	@Autowired
	private UserService userService;
	
	@Override
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserDTO userDto) {
		this.userService.save(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Usuário cadastrado com sucesso!"));
	}
	
	@Override 
	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
		userService.update(userUpdateDTO, id);
		return ResponseEntity.ok(Map.of("mensagem", "Usuário atualizado com sucesso."));
	}

	@Override 
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Long id){
		userService.delete(id);
		return ResponseEntity.ok(Map.of("mensagem", "Usuário excluído com sucesso."));
	}

	@Override 
	@GetMapping("/all")
	public ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(Pageable pageable) {
		PaginatedResponseDTO<UserResponseDTO> userPage = userService.getAllUsers(pageable);
		return ResponseEntity.ok(userPage);
	}

	@Override 
	@GetMapping("/detail/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PatchMapping("/update-partial/{id}")
	public ResponseEntity<Map<String, String>> updatePartial(
			@PathVariable Long id,
			@RequestBody UserPartialUpdateDTO dto) {

		userService.updatePartial(id, dto);
		return ResponseEntity.ok(Map.of("message", "Campo atualizado com sucesso."));
	}


	@PatchMapping("/update-password/{id}")
	public ResponseEntity<Map<String, String>> updatePassword(
			@PathVariable Long id,
			@Valid @RequestBody PasswordUpdateDTO dto) {
		userService.updatePassword(id, dto);
		return ResponseEntity.ok(Map.of("mensagem", "Senha atualizada com sucesso."));
	}

}