package br.com.fiap.interfaces.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;


public interface UserService {

	void save(UserDTO userDto);

	void update(UserDTO userDto, Long id);

	void delete(Long id);

	Page<UserResponseDTO> getAllUsers(Pageable pageable);

	UserResponseDTO getUserById(Long id);

}
