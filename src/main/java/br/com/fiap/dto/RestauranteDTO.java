package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record RestauranteDTO(
		@NotNull(message = "O nome precisa ser informado")
		String nome,
		@NotNull(message = "O endereco precisa deve ser informado")
		String endereco,
		@NotNull(message = "O tipo de cozinha deve ser informado")
		String tipoCozinha,
		@NotNull(message = "O horario Funcionamento deve ser informado")
		String horarioFuncionamento) {
}
