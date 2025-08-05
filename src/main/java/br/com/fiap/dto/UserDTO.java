package br.com.fiap.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserDTO(

		@Email(message = "E-mail inválido")
		@NotBlank(message = "O e-mail é obrigatório")
		String email,

		@NotBlank(message = "A senha é obrigatória")
		@Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
		@Pattern(
				regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
				message = "A nova senha deve conter letras e números."
		)
		String password,

		@NotBlank(message = "O nome é obrigatório")
		String name,

		@Valid @NotNull(message = "O endereço é obrigatório")
		AddressDTO address,

		@NotBlank(message = "O login é obrigatório")
		@Pattern(regexp = "^[^@]+$", message = "O login não pode conter '@'")
		String login

) {}
