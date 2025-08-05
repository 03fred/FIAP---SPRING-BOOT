package br.com.fiap.dto;

import java.time.LocalTime;

public record RestaurantResponseDTO(
		String name,
		String adress,
		String typeKitchen,
		LocalTime OpeningTime,
		LocalTime ClosingTime) {
}
