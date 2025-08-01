package br.com.fiap.services;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.PasswordUpdateDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserPartialUpdateDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.exceptions.ConflictException;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.factory.AddressFactory;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import br.com.fiap.model.enums.EnumUserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.fiap.factory.UserFactory.UserUpdateDTOMock;
import static br.com.fiap.factory.UserFactory.createUserDTOMock;
import static br.com.fiap.factory.UserFactory.createUserMock;
import static br.com.fiap.factory.UserFactory.getUser;
import static br.com.fiap.factory.UserFactory.getUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldSaveUserSuccessfully() {
        UserDTO userDTO = createUserDTOMock();
        Role userRole = new Role(EnumUserType.USER.toString());
        when(userRepository.existsByEmail(userDTO.email())).thenReturn(false);
        when(userRepository.existsByLogin(userDTO.login())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.password())).thenReturn("senha-criptografada123");
        when(roleRepository.findByName(EnumUserType.USER.toString())).thenReturn(Optional.of(userRole));
        
        userService.save(userDTO);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenEmailExists() {
        UserDTO userDTO = createUserDTOMock();
        when(userRepository.existsByEmail(userDTO.email())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.save(userDTO));
    }

    @Test
    void shouldThrowConflictExceptionWhenLoginExists() {
        UserDTO userDTO = createUserDTOMock();
        when(userRepository.existsByEmail(userDTO.email())).thenReturn(false);
        when(userRepository.existsByLogin(userDTO.login())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.save(userDTO));
    }

    @Test
    void shouldUpdateUser() {
        Long userId = 1L;
        User user = createUserMock();
        UserUpdateDTO dto = UserUpdateDTOMock();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.update(dto, userId);

        verify(userRepository).save(user);
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.name(), user.getName());
        assertEquals(dto.login(), user.getLogin());
        assertEquals(dto.address(), user.getAddress());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserUpdateDTO dto = UserUpdateDTOMock();

        assertThrows(ResourceNotFoundException.class, () -> userService.update(dto, 1L));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(1L));
    }

    @Test
    void shouldReturnAllUsersPaginated() {
        User user = new User();
        user.setId(1L);
        user.setEmail("a@a.com");
        user.setName("Ana");
        user.setAddress(AddressFactory.getMockAddress(user));
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);

        PaginatedResponseDTO<UserResponseDTO> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Ana", result.getContent().get(0).name());
    }

    @Test
    void shouldReturnUserById() {
        User user = createUserMock();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO dto = userService.getUserById(1L);

        assertEquals(user.getId(), dto.id());
        assertEquals(user.getEmail(), dto.email());
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        User user = createUserMock();
        PasswordUpdateDTO dto = getUpdateDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.oldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn("senhaNovaCriptografada");

        userService.updatePassword(1L, dto);

        verify(userRepository).save(user);
        assertEquals("senhaNovaCriptografada", user.getPassword());
    }

    @Test
    void shouldThrowIfOldPasswordDoesNotMatch() {
        User user = createUserMock();
        PasswordUpdateDTO dto = getUpdateDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.oldPassword(), user.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(1L, dto));
    }

    private PasswordUpdateDTO getUpdateDto() {
        return new PasswordUpdateDTO("errada", "novaSenha", "novaSenha");
    }

    @Test
    void shouldThrowIfPasswordsDoNotMatch() {
        User user = createUserMock();
        PasswordUpdateDTO dto = new PasswordUpdateDTO("old123", "novaSenha", "outraSenha");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.oldPassword(), user.getPassword())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(1L, dto));
    }

    @Test
    void shouldReturnOptionalUser() {
        User user = createUserMock();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void shouldThrowWhenGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void shouldUpdateUserPartially() {
        User user = createUserMock();
        UserPartialUpdateDTO dto = new UserPartialUpdateDTO("Nova Ana", "novo@email.com", "rua nova", AddressFactory.getMockAddressDTO());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByLogin(dto.login())).thenReturn(false);

        userService.updatePartial(1L, dto);

        assertEquals(dto.name(), user.getName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.login(), user.getLogin());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenPartialUpdateEmailExists() {
        User user = createUserMock();
        UserPartialUpdateDTO dto = new UserPartialUpdateDTO("Ana", "duplicado@email.com", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.updatePartial(1L, dto));
    }

    @Test
    void shouldThrowWhenPartialUpdateLoginExists() {
        User user = createUserMock();
        user.setLogin("originalLogin");

        UserPartialUpdateDTO dto = new UserPartialUpdateDTO(
                "Novo Nome",
                "novo@email.com",
                "loginEmUso",
                AddressFactory.getMockAddressDTO()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByLogin("loginEmUso")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.updatePartial(1L, dto));
    }

    @Test
    void shouldThrowWhenNoFieldsToUpdate() {
        User user = createUserMock();
        UserPartialUpdateDTO dto = new UserPartialUpdateDTO(" ", " ", " ", AddressFactory.getEmptyAddressDTO());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.updatePartial(1L, dto));
    }

    @Test
    void testConstructorFromUserDTO() {
        UserDTO userDTO = getUserDTO();

        User user = new User(userDTO);

        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("johndoe", user.getLogin());
        assertEquals("Rua das Flores, 123", user.getAddress().getStreet());
        assertNotNull(user.getDtUpdateRow());
    }

    @Test
    void testAllArgsConstructor() {
        Role role = new Role("USER");
        Set<Role> roles = Set.of(role);
        List<Restaurant> restaurants = new ArrayList<>();
        Date updateDate = new Date();

        User user = getUser(roles, restaurants, updateDate);

        assertEquals(1L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("janedoe", user.getLogin());
        assertEquals("pass123", user.getPassword());
        assertEquals(updateDate, user.getDtUpdateRow());
        assertEquals(AddressFactory.getMockAddress(), user.getAddress());
        assertEquals(roles, user.getUserTypesRoles());
        assertEquals(restaurants, user.getRestaurant());
    }

    @Test
    void testRemoveRole() {
        Role role1 = new Role("USER");
        Role role2 = new Role("ADMIN");

        User user = new User();
        user.getUserTypesRoles().add(role1);
        user.getUserTypesRoles().add(role2);

        assertEquals(2, user.getUserTypesRoles().size());

        user.removeRole(role1);

        assertEquals(1, user.getUserTypesRoles().size());
        assertFalse(user.getUserTypesRoles().contains(role1));
        assertTrue(user.getUserTypesRoles().contains(role2));
    }

    @Test
    void testOnUpdate() throws InterruptedException {
        User user = new User();
        Date beforeUpdate = new Date();

        Thread.sleep(10);
        user.onUpdate();
        Date afterUpdate = user.getDtUpdateRow();

        assertNotNull(afterUpdate);
        assertTrue(afterUpdate.after(beforeUpdate));
    }
}
