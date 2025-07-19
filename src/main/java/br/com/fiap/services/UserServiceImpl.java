package br.com.fiap.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.exceptions.ConflictException;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import br.com.fiap.model.enums.EnumUserType;
import jakarta.transaction.Transactional;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void save(UserDTO userDto) {

		if (userRepository.existsByEmail(userDto.email())) {
			throw new ConflictException("Email já existente");
		}

		if (userRepository.existsByLogin(userDto.login())) {
			throw new ConflictException("Login já existente");
		}

		String passwordCrypto = this.passwordEncoder.encode(userDto.password());
		User user = new User(userDto, passwordCrypto);
		
		Role role = this.roleRepository.findByName(EnumUserType.USER.toString())
				.orElseGet(() -> roleRepository.save(new Role(EnumUserType.USER.toString())));
		user.setUserTypesRoles(Set.of(role));
		
		var save = this.userRepository.save(user);
		
		Assert.notNull(save, "Erro ao salvar o usuário com o email: " + user.getEmail() + ".");
	}

	@Override
	public void update(UserDTO userDto, Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

		User userUpadte = getUser(userDto, user);
		var save = this.userRepository.save(userUpadte);
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByLogin(username)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + username));
	}

	@Override
	public void update(UserDTO userDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userAuth = (User) authentication.getPrincipal();
		User user = userRepository.findByLogin(userAuth.getLogin())
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userAuth.getLogin()));
		
       User userUpadte = getUser(userDto, user);
       var save = this.userRepository.save(userUpadte);
		Assert.notNull(save, "Erro ao atualizaro o usuário com o email: " + user.getEmail() + ".");
	}
	
	private User getUser(UserDTO userDto, User user) {
		String passwordCrypto = this.passwordEncoder.encode(userDto.password());
		user.setEmail(userDto.email());
		user.setPassword(passwordCrypto);
		user.setName(userDto.name());
		user.setAddress(userDto.address());
		user.setLogin(userDto.login());
		return user;
	}

}
