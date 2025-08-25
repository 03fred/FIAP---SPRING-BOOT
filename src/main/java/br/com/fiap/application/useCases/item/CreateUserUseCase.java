package br.com.fiap.application.useCases.item;

import br.com.fiap.domain.entities.User;
import br.com.fiap.gateways.UserRepository;

public class CreateUserUseCase {

	private final UserRepository repository;

	public CreateUserUseCase(UserRepository repository) {
		this.repository = repository;
	}

	public User execute(User user) {
		return repository.save(user);
	}
}
