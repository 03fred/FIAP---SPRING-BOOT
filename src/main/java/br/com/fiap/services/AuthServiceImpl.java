package br.com.fiap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.AuthService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.User;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

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
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o e-mail: " + email));
	}

	@Override
	public User getUserByLogin(String login) {
		return userRepository.findByLogin(login)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + login));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + username));
		
		return  new AuthenticatedUser(null, user.getName(), user.getLogin(),
				user.getId(), user.getUserTypesRoles());
	}
}
