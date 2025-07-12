package br.com.fiap.services;

import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static br.com.fiap.factory.UserFactory.createUserMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldVerifyPasswordSuccessfully() {
        String rawPassword = "123456";
        String encodedPassword = "$2a$10$encoded";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = authService.verifyPassword(rawPassword, encodedPassword);

        assertTrue(result);
    }

    @Test
    void shouldReturnUserByEmail() {
        User user = createUserMock();
        when(userRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(user));

        User result = authService.getUserByEmail("ana@email.com");

        assertNotNull(result);
        assertEquals("ana@email.com", result.getEmail());
    }

    @Test
    void shouldThrowWhenEmailNotFound() {
        when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.getUserByEmail("notfound@email.com"));
    }

    @Test
    void shouldReturnUserByLogin() {
        User user = createUserMock();
        when(userRepository.findByLogin("anaLogin")).thenReturn(Optional.of(user));

        User result = authService.getUserByLogin("anaLogin");

        assertNotNull(result);
        assertEquals("anaLogin", result.getLogin());
    }

    @Test
    void shouldThrowWhenLoginNotFound() {
        when(userRepository.findByLogin("notfound")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.getUserByLogin("notfound"));
    }

}