package br.com.fiap.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.fiap.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	public void update(UserUpdateDTO userUpdateDTO, Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
		
		user.setEmail(userUpdateDTO.email());
		user.setName(userUpdateDTO.name());
		user.setAddress(userUpdateDTO.address());
		user.setLogin(userUpdateDTO.login());
		
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

	@Override
	public Optional<User> getUser(Long id) {
		return Optional.ofNullable(userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id)));
	}

	@Override
	public void updatePartial(Long id, UserPartialUpdateDTO dto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		boolean changed = false;

		if (dto.name() != null && !dto.name().isBlank()) {
			user.setName(dto.name());
			changed = true;
		}

		if (dto.email() != null && !dto.email().isBlank()) {
			if (userRepository.existsByEmail(dto.email()) && !dto.email().equals(user.getEmail())) {
				throw new ConflictException("E-mail já existente.");
			}
			user.setEmail(dto.email());
			changed = true;
		}

		if (dto.address() != null && !dto.address().isBlank()) {
			user.setAddress(dto.address());
			changed = true;
		}

		if (dto.login() != null && !dto.login().isBlank()) {
			if (userRepository.existsByLogin(dto.login()) && !dto.login().equals(user.getLogin())) {
				throw new ConflictException("Login já existente.");
			}
			user.setLogin(dto.login());
			changed = true;
		}

		if (!changed) {
			throw new IllegalArgumentException("Nenhum campo válido para atualizar foi enviado.");
		}

		userRepository.save(user);
	}

	@Override
	public void updatePassword(Long id, PasswordUpdateDTO dto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Senha atual incorreta.");
		}

		if (!dto.newPassword().equals(dto.confirmPassword())) {
			throw new IllegalArgumentException("As senhas não coincidem.");
		}

		String newEncryptedPassword = passwordEncoder.encode(dto.newPassword());
		user.setPassword(newEncryptedPassword);
		userRepository.save(user);

	}


	}
