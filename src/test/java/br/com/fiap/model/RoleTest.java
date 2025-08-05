package br.com.fiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void shouldCreateRoleWithNameOnlyConstructor() {
        Role role = new Role("ADMIN");
        assertEquals("ADMIN", role.getName());
        assertNull(role.getId());
    }

    @Test
    void shouldCreateRoleWithAllArgsConstructor() {
        Role role = new Role(1L, "USER");
        assertEquals(1L, role.getId());
        assertEquals("USER", role.getName());
    }

    @Test
    void shouldSetAndGetValues() {
        Role role = new Role();
        role.setId(2L);
        role.setName("MANAGER");

        assertEquals(2L, role.getId());
        assertEquals("MANAGER", role.getName());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Role r1 = new Role(1L, "ADMIN");
        Role r2 = new Role(1L, "ADMIN");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        Set<Role> roles = new HashSet<>();
        roles.add(r1);
        assertTrue(roles.contains(r2));
    }

    @Test
    void shouldNotBeEqualWithDifferentIds() {
        Role r1 = new Role(1L, "ADMIN");
        Role r2 = new Role(2L, "ADMIN");

        assertNotEquals(r1, r2);
    }

    @Test
    void shouldUseToString() {
        Role role = new Role(1L, "RESTAURANT_OWNER");
        String result = role.toString();
        assertTrue(result.contains("RESTAURANT_OWNER"));
        assertTrue(result.contains("1"));
    }
}
