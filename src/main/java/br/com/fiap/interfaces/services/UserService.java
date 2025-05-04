package br.com.fiap.interfaces.services;

import java.util.Optional;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.model.User;

public interface UserService {
	
	void save(UserDTO userDto);
	
	Optional<User> getUserById(Long id);

	User getUserByEmail(String email);

	boolean verifyPassword(String password, String passwordDatabase);
	
	void update(UserDTO userDto, Long id);

	void delete(Long id);
}
