package br.com.fiap.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.User;
import br.com.fiap.model.enums.UserType;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));
	}

	@Override
	public boolean verifyPassword(String password, String passwordDatabase) {
		return passwordEncoder.matches(password, passwordDatabase);
	}

	@Override
	public void save(UserDTO userDto) {
		String passwordCrypto = this.passwordEncoder.encode(userDto.password());
		User user = new User(userDto, UserType.USER, passwordCrypto);
		var save = this.userRepository.save(user);
		Assert.notNull(save, "Erro ao salvar o usuário com o email: " + user.getEmail() + ".");
	}

	@Override
	public void update(UserDTO userDto, Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
		
		String passwordCrypto = this.passwordEncoder.encode(userDto.password());
		
		user.setEmail(userDto.email());
		user.setLogin(userDto.email());
		user.setName(userDto.name());
		user.setPassword(passwordCrypto);
		
		var save = this.userRepository.save(user);
		Assert.notNull(save, "Erro ao atualizaro o usuário com o email: " + user.getEmail() + ".");
		
	}

	public void delete(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(
				() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id + "."));
		userRepository.delete(user);
	}
}
