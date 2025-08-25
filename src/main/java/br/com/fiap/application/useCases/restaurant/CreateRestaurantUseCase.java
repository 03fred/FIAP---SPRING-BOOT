package br.com.fiap.application.useCases.restaurant;

import br.com.fiap.domain.entities.User;
import br.com.fiap.gateways.UserRepository;

public class CreateRestaurantUseCase {

	private final UserRepository repository;

	public CreateRestaurantUseCase(UserRepository repository) {
		this.repository = repository;
	}

	public User execute(User user) {
		return repository.save(user);
	}
}
