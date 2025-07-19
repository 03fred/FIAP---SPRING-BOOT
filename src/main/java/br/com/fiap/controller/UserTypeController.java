package br.com.fiap.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.dto.UserTypeDTO;
import br.com.fiap.interfaces.services.UserTypeService;
import br.com.fiap.interfaces.swagger.UserTypeApi;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users_type", produces = { "application/json" })
public class UserTypeController implements UserTypeApi{

	private final UserTypeService service;

	public UserTypeController(UserTypeService service) {
		this.service = service;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserTypeDTO userType) {
		this.service.save(userType);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Permissão cadastrada com sucesso!"));
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping
	public ResponseEntity<Map<String, String>> delete(@Valid @RequestBody UserTypeDTO userType) {
		this.service.removeRoleFromUser(userType);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("mensagem", "Permissão removida com sucesso!"));
	}
}