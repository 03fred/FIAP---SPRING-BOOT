package br.com.fiap.domain.entities;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetToken {

	private Long id;

	private String token;

	private User user;

	private LocalDateTime expiration;

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiration);
	}

}
