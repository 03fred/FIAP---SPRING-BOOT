package br.com.fiap.application.useCases.menu;

import br.com.fiap.domain.entities.User;
import br.com.fiap.gateways.UserRepository;

public class CreateMenuUseCase {

	private final UserRepository repository;

	public CreateMenuUseCase(UserRepository repository) {
		this.repository = repository;
	}

	public User execute(User user) {
		return repository.save(user);
	}
}
