package br.com.fiap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.interfaces.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody UserDTO userDto) {
		this.userService.save(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDto) {
		this.userService.update(userDto, id);
		return ResponseEntity.status(204).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
