package br.com.fiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordResetTokenTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        LocalDateTime expiration = LocalDateTime.now().plusHours(2);
        PasswordResetToken token = new PasswordResetToken(100L, "abc123", user, expiration);

        assertEquals(100L, token.getId());
        assertEquals("abc123", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(expiration, token.getExpiration());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        PasswordResetToken token = new PasswordResetToken();
        token.setId(200L);
        token.setToken("xyz789");

        User user = new User();
        user.setId(2L);
        token.setUser(user);

        LocalDateTime expiration = LocalDateTime.now().plusDays(1);
        token.setExpiration(expiration);

        assertEquals(200L, token.getId());
        assertEquals("xyz789", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(expiration, token.getExpiration());
    }

    @Test
    void testIsExpiredTrue() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiration(LocalDateTime.now().minusMinutes(5));

        assertTrue(token.isExpired());
    }

    @Test
    void testIsExpiredFalse() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiration(LocalDateTime.now().plusMinutes(5));

        assertFalse(token.isExpired());
    }

    @Test
    void testEqualsAndHashCode() {
        User user = new User();
        user.setId(1L);

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);
        PasswordResetToken token1 = new PasswordResetToken(1L, "abc123", user, expiration);
        PasswordResetToken token2 = new PasswordResetToken(1L, "abc123", user, expiration);

        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(3L);

        PasswordResetToken token = new PasswordResetToken(3L, "tokenXYZ", user, LocalDateTime.now());

        String result = token.toString();
        assertTrue(result.contains("tokenXYZ"));
        assertTrue(result.contains("3")); // ID
    }

}