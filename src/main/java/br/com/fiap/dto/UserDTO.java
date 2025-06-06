package br.com.fiap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserDTO(

		@Email(message = "E-mail inválido")
		@NotBlank(message = "O e-mail é obrigatório")
		String email,

		@NotBlank(message = "A senha é obrigatória")
		String password,

		@NotBlank(message = "O nome é obrigatório")
		String name,

		@NotBlank(message = "O endereço é obrigatório")
		String address,

		@NotBlank(message = "O login é obrigatório")
		@Pattern(regexp = "^[^@]+$", message = "O login não pode conter '@'")
		String login

) {}
