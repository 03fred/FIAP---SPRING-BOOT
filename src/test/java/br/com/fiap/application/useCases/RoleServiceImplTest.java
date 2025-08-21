package br.com.fiap.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.dto.UserTypeDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.domain.repositories.RoleRepository;
import br.com.fiap.domain.repositories.UserRepository;
import br.com.fiap.application.useCases.RoleServiceImpl;
import br.com.fiap.domain.entities.Role;
import br.com.fiap.domain.entities.User;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void shouldAddRoleToUserSuccessfully() {
        User user = new User();
        Role role = new Role("ADMIN");

        UserTypeDTO dto = new UserTypeDTO("johndoe", "ADMIN");

        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(dto.role())).thenReturn(Optional.of(role));

        roleService.save(dto);

        assertTrue(user.getUserTypesRoles().contains(role));
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenRoleNotFound() {
        UserTypeDTO dto = new UserTypeDTO("johndoe", "NON_EXISTENT_ROLE");

        when(roleRepository.findByName(dto.role())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> roleService.save(dto));
        assertEquals("Permissão não encontrada com o nome: NON_EXISTENT_ROLE", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUserNotFoundOnSaveRole() {
        UserTypeDTO dto = new UserTypeDTO("johndoe", "ADMIN");
        when(roleRepository.findByName(dto.role())).thenReturn(Optional.of(new Role("ADMIN")));
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> roleService.save(dto));
        assertEquals("Usuário não encontrado com o login: johndoe", ex.getMessage());
    }

    @Test
    void shouldRemoveRoleFromUserSuccessfully() {
        Role role = new Role("ADMIN");
        User user = new User();
        user.getUserTypesRoles().add(role);

        UserTypeDTO dto = new UserTypeDTO("johndoe", "ADMIN");

        when(roleRepository.findByName(dto.role())).thenReturn(Optional.of(role));
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.of(user));

        roleService.removeRoleFromUser(dto);

        assertFalse(user.getUserTypesRoles().contains(role));
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenRoleNotFoundOnRemove() {
        UserTypeDTO dto = new UserTypeDTO("johndoe", "ADMIN");

        when(roleRepository.findByName(dto.role())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.removeRoleFromUser(dto));
    }

    @Test
    void shouldThrowWhenUserNotFoundOnRemoveRole() {
        UserTypeDTO dto = new UserTypeDTO("johndoe", "ADMIN");

        when(roleRepository.findByName(dto.role())).thenReturn(Optional.of(new Role("ADMIN")));
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.removeRoleFromUser(dto));
    }
}