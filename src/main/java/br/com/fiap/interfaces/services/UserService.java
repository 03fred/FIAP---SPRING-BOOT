package br.com.fiap.interfaces.services;

import br.com.fiap.dto.*;
import br.com.fiap.model.User;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserService {

	void save(UserDTO userDto);

	void update(UserUpdateDTO userUpdateDTO, Long id);

	void delete(Long id);

	void updatePassword(Long id, PasswordUpdateDTO dto);

	void updatePartial(Long id, UserPartialUpdateDTO dto);

	PaginatedResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable);

	UserResponseDTO getUserById(Long id);

	Optional<User> getUser(Long id);

}
