package br.com.fiap.services;


import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.AuthService;

import br.com.fiap.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean verifyPassword(String password, String passwordDatabase) {
		return passwordEncoder.matches(password, passwordDatabase);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));
	}

}
