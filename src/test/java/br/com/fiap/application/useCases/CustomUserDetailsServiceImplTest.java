package br.com.fiap.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.fiap.domain.repositories.UserRepository;
import br.com.fiap.application.useCases.CustomUserDetailsServiceImpl;
import br.com.fiap.domain.entities.Role;
import br.com.fiap.domain.entities.User;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsServiceImpl userDetailsService;

    private User mockUser;

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setName("ADMIN");

        mockUser = new User();
        mockUser.setName("admin_login");
        mockUser.setPassword("secure_password");
        mockUser.setUserTypesRoles(Set.of(role));
    }

    @Test
    void whenUserExists_thenReturnUserDetails() {
        // given
        when(userRepository.findByLogin("admin_login")).thenReturn(Optional.of(mockUser));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin_login");

        // then
        assertEquals("admin_login", userDetails.getUsername());
        assertEquals("secure_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByLogin("admin_login");
    }

    @Test
    void whenUserDoesNotExist_thenThrowUsernameNotFoundException() {
        when(userRepository.findByLogin("unknown_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown_user");
        });

        verify(userRepository, times(1)).findByLogin("unknown_user");
    }
}
