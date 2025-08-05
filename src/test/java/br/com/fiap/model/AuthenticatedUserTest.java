package br.com.fiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class AuthenticatedUserTest {

    @Test
    void shouldCreateUserAndReturnAuthorities() {
        Role adminRole = new Role(1L, "ADMIN");
        Role userRole = new Role(2L, "USER");

        AuthenticatedUser user = new AuthenticatedUser(
                100L,
                "adminUser",
                "admin",
                1L,
                Set.of(adminRole, userRole)
        );

        // Verifica campos básicos
        assertEquals(100L, user.getRestaurantId());
        assertEquals("adminUser", user.getUsername());
        assertEquals("admin", user.getLogin());
        assertEquals(1L, user.getId());
        assertEquals(2, user.getUserTypesRoles().size());

        // Verifica authorities
        var authorities = user.getAuthorities();
        assertEquals(2, authorities.size());

        Set<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertTrue(authorityNames.contains("ROLE_ADMIN"));
        assertTrue(authorityNames.contains("ROLE_USER"));
    }

    @Test
    void shouldReturnTrueIfHasRoleAdmin() {
        Role adminRole = new Role(1L, "ADMIN");

        AuthenticatedUser user = new AuthenticatedUser(
                200L, "admin", "admin", 2L, Set.of(adminRole)
        );

        assertTrue(user.hasRoleAdmin());
    }

    @Test
    void shouldReturnFalseIfNoRoleAdmin() {
        Role userRole = new Role(1L, "USER");

        AuthenticatedUser user = new AuthenticatedUser(
                200L, "user", "user", 3L, Set.of(userRole)
        );

        assertFalse(user.hasRoleAdmin());
    }
}