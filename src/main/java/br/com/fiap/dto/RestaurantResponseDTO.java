package br.com.fiap.dto;

public record RestaurantResponseDTO(
		String name,
		String adress,
		String typeKitchen,
		String openingHours) {
}
