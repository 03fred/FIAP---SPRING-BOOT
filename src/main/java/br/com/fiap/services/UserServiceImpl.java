package br.com.fiap.services;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.exceptions.ConflictException;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.User;
import br.com.fiap.model.enums.EnumType;
import org.springframework.util.Assert;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void save(UserDTO userDto) {

		if (userRepository.existsByEmail(userDto.email())) {
			throw new ConflictException("Email já existente");
		}

		if (userRepository.existsByLogin(userDto.login())) {
			throw new ConflictException("Login já existente");
		}

		String passwordCrypto = this.passwordEncoder.encode(userDto.password());
		User user = new User(userDto, EnumType.USER, passwordCrypto);
		var save = this.userRepository.save(user);
//		Assert.notNull(save, "Erro ao salvar o usuário com o email: " + user.getEmail() + ".");
	}

	@Override
	public void update(UserDTO userDto, Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
		
		String passwordCrypto = this.passwordEncoder.encode(userDto.password());
		
		user.setEmail(userDto.email());
		user.setPassword(passwordCrypto);
		user.setName(userDto.name());
		user.setAddress(userDto.address());
		user.setLogin(userDto.login());
		
		var save = this.userRepository.save(user);
		Assert.notNull(save, "Erro ao atualizaro o usuário com o email: " + user.getEmail() + ".");
		
	}

	@Override
	public void delete(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(
				() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id + "."));
		userRepository.delete(user);
	}

	public PaginatedResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable) {
		Page<User> userPage = userRepository.findAll(pageable);
		List<UserResponseDTO> userResponseDTOs = userPage.getContent().stream()
				.map(user -> new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getAddress()))
				.collect(Collectors.toList());
		
		return new PaginatedResponseDTO<>(
                userResponseDTOs,
                userPage.getTotalElements(),
                userPage.getNumber(), 
                userPage.getSize()   
        );
	}
	
	@Override
	public UserResponseDTO getUserById(Long id) {
		var user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		return new UserResponseDTO(
				user.getId(),
				user.getEmail(),
				user.getName(),
				user.getAddress()
		);
	}

}
