package br.com.fiap.interfaces.services;

import java.util.List;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;


public interface UserService {

	void save(UserDTO userDto);

	void update(UserDTO userDto, Long id);

	void delete(Long id);

	List<UserResponseDTO> findAll();

	UserResponseDTO getUserById(Long id);

}
