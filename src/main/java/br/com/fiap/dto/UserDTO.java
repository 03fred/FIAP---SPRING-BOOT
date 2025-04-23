package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record UserDTO(
		@NotNull(message = "O Email não pode ser null") 
		String email,
		@NotNull(message = "A senha não pode ser null") 
		String password, 
		@NotNull(message = "O nome não pode ser null")
		String name,
		@NotNull(message = "O Endereço não pode ser null")
		String address,
		@NotNull(message = "O Login não pode ser null")
		String login
		
		) {
}
