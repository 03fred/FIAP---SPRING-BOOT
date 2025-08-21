package br.com.fiap.application.useCases;


import java.util.Optional;

import org.springframework.data.domain.Pageable;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.PasswordUpdateDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserPartialUpdateDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.domain.entities.User;


public interface UserService {

	void save(UserDTO userDto);

	void update(UserUpdateDTO userUpdateDTO, Long id);

	void delete(Long id);

	void updatePassword(Long id, PasswordUpdateDTO dto);

	void updatePartial(Long id, UserPartialUpdateDTO dto);

	PaginatedResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable);

	UserResponseDTO getUserById(Long id);

	Optional<User> getUser(Long id);
	
	void update(UserDTO userDto);

}
