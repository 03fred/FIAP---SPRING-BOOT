package br.com.fiap.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record RestaurantDTO(
		@NotNull(message = "O nome precisa ser informado")
		String name,
		@NotNull(message = "O endereco precisa deve ser informado")
		String adress,
		@NotNull(message = "O tipo de cozinha deve ser informado")
		String typeKitchen,

		@JsonFormat(pattern = "HH:mm")
		@NotNull(message = "O horario ínicio de Funcionamento deve ser informado")
		LocalTime openingTime,

		@JsonFormat(pattern = "HH:mm")
		@NotNull(message = "O horario fim de Funcionamento deve ser informado")
		LocalTime closingTime){
}
