package br.com.fiap.application.useCases;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.fiap.domain.gateways.CryptographyGateway;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.fiap.dto.AddressDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.PasswordUpdateDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserPartialUpdateDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.exceptions.ConflictException;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.domain.gateways.RoleDatabaseGateway;
import br.com.fiap.domain.gateways.UserDatabaseGateway;
import br.com.fiap.mapper.AddressMapper;
import br.com.fiap.application.useCases.UserService;
import br.com.fiap.domain.entities.AuthenticatedUser;
import br.com.fiap.domain.entities.Role;
import br.com.fiap.domain.entities.User;
import br.com.fiap.domain.enums.EnumUserType;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
	private final UserDatabaseGateway userDatabaseGateway;
	private final RoleDatabaseGateway roleDatabaseGateway;
	private final CryptographyGateway cryptographyGateway;

	public UserServiceImpl(UserDatabaseGateway userDatabaseGateway,
					   RoleDatabaseGateway roleDatabaseGateway,
					   CryptographyGateway cryptographyGateway) {
		this.userDatabaseGateway = userDatabaseGateway;
		this.roleDatabaseGateway = roleDatabaseGateway;
		this.cryptographyGateway = cryptographyGateway;
	}

	@Override
	@Transactional
	public void save(UserDTO userDto) {

		if (userDatabaseGateway.existsByEmail(userDto.email())) {
			throw new ConflictException("Email já existente");
		}

		if (userDatabaseGateway.existsByLogin(userDto.login())) {
			throw new ConflictException("Login já existente");
		}

		String passwordCrypto = this.cryptographyGateway.encodePassword(userDto.password());
		User user = new User(userDto, passwordCrypto);

		Role role = this.roleDatabaseGateway.findByName(EnumUserType.USER.toString())
				.orElseGet(() -> roleDatabaseGateway.save(new Role(EnumUserType.USER.toString())));
		user.setUserTypesRoles(Set.of(role));

		var save = this.userDatabaseGateway.save(user);
	}

	@Override
	public void update(UserUpdateDTO userUpdateDTO, Long id) {
		User user = userDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		user.setEmail(userUpdateDTO.email());
		user.setName(userUpdateDTO.name());
		user.setLogin(userUpdateDTO.login());
		AddressDTO ad = userUpdateDTO.address();
		user.setAddress(AddressMapper.toEntity(userUpdateDTO.address(), user)
);
		var save = this.userDatabaseGateway.save(user);
		Assert.notNull(save, "Erro ao atualizaro o usuário com o email: " + user.getEmail() + ".");

	}

	@Override
	public void delete(Long id) {
		User user = userDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id + "."));
		userDatabaseGateway.delete(user);
	}

	public PaginatedResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable) {
		Page<User> userPage = userDatabaseGateway.findAll(pageable);

		List<UserResponseDTO> userResponseDTOs = userPage.getContent().stream()
				.map(user -> new UserResponseDTO(
						user.getId(),
						user.getEmail(),
						user.getName(),
						AddressMapper.toDTO(user.getAddress())
				))
				.collect(Collectors.toList());

		return new PaginatedResponseDTO<>(userResponseDTOs,
				userPage.getTotalElements(),
				userPage.getNumber(),
				userPage.getSize());
	}


	@Override
	public UserResponseDTO getUserById(Long id) {
		var user = userDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		return new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), AddressMapper.toDTO(user.getAddress()));
	}

	@Override
	public Optional<User> getUser(Long id) {
		return Optional.ofNullable(userDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id)));
	}

	@Override
	public void update(UserDTO userDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AuthenticatedUser userAuth = (AuthenticatedUser) authentication.getPrincipal();
		User user = userDatabaseGateway.findByLogin(userAuth.getLogin()).orElseThrow(
				() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userAuth.getLogin()));

		User userUpadte = getUser(userDto, user);
		var save = this.userDatabaseGateway.save(userUpadte);
		Assert.notNull(save, "Erro ao atualizaro o usuário com o email: " + user.getEmail() + ".");
	}

	private User getUser(UserDTO userDto, User user) {
		String passwordCrypto = this.cryptographyGateway.encodePassword(userDto.password());
		user.setEmail(userDto.email());
		user.setPassword(passwordCrypto);
		user.setName(userDto.name());
		user.setAddress(AddressMapper.toEntity(userDto.address(), user));
		user.setLogin(userDto.login());
		return user;
	}

	public void updatePartial(Long id, UserPartialUpdateDTO dto) {
		User user = userDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		boolean changed = false;

		if (dto.name() != null && !dto.name().isBlank()) {
			user.setName(dto.name());
			changed = true;
		}

		if (dto.email() != null && !dto.email().isBlank()) {
			if (userDatabaseGateway.existsByEmail(dto.email()) && !dto.email().equals(user.getEmail())) {
				throw new ConflictException("E-mail já existente.");
			}
			user.setEmail(dto.email());
			changed = true;
		}

		if (dto.address() != null) {
			user.setAddress(AddressMapper.toEntity(dto.address(), user));
			changed = true;
		}

		if (dto.login() != null && !dto.login().isBlank()) {
			if (userDatabaseGateway.existsByLogin(dto.login()) && !dto.login().equals(user.getLogin())) {
				throw new ConflictException("Login já existente.");
			}
			user.setLogin(dto.login());
			changed = true;
		}

		if ((dto.name() == null || dto.name().isBlank()) &&
				(dto.email() == null || dto.email().isBlank()) &&
				(dto.login() == null || dto.login().isBlank()) &&
				(dto.address() == null || AddressMapper.isEmpty(dto.address()))) {
			throw new IllegalArgumentException("Nenhum campo válido para atualizar foi enviado.");
		}


		userDatabaseGateway.save(user);
	}

	@Override
	public void updatePassword(Long id, PasswordUpdateDTO dto) {
		User user = userDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		if (!cryptographyGateway.verifyPassword(dto.oldPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Senha atual incorreta.");
		}

		if (!dto.newPassword().equals(dto.confirmPassword())) {
			throw new IllegalArgumentException("As senhas não coincidem.");
		}

		String newEncryptedPassword = cryptographyGateway.encodePassword(dto.newPassword());
		user.setPassword(newEncryptedPassword);
		userDatabaseGateway.save(user);

	}
}
