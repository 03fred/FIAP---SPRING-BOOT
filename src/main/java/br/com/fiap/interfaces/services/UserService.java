package br.com.fiap.interfaces.services;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.model.User;


public interface UserService {

	void save(UserDTO userDto);

	void update(UserDTO userDto, Long id);

	void delete(Long id);

	PaginatedResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable);

	UserResponseDTO getUserById(Long id);

	Optional<User> getUser(Long id);
	
	void update(UserDTO userDto);

}
