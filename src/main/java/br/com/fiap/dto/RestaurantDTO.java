package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record RestaurantDTO(
		@NotNull(message = "O nome precisa ser informado")
		String name,
		@NotNull(message = "O endereco precisa deve ser informado")
		String adress,
		@NotNull(message = "O tipo de cozinha deve ser informado")
		String typeKitchen,
		@NotNull(message = "O horario Funcionamento deve ser informado")
		String openingHours) {
}
