package br.com.fiap.interfaces.services;

import java.util.List;
import java.util.Optional;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.model.User;


public interface UserService {

	void save(UserDTO userDto);

	User getUserByEmail(String email);

	boolean verifyPassword(String password, String passwordDatabase);

	void update(UserDTO userDto, Long id);

	void delete(Long id);

	List<UserResponseDTO> findAll();

	UserResponseDTO getUserById(Long id);

}
