package br.com.fiap.application.useCases;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.UserAuthorizationDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.domain.gateways.PasswordResetTokenDatabaseGateway;
import br.com.fiap.domain.gateways.UserDatabaseGateway;
import br.com.fiap.application.useCases.AuthService;
import br.com.fiap.application.useCases.EmailService;
import br.com.fiap.domain.entities.AuthenticatedUser;
import br.com.fiap.domain.entities.PasswordResetToken;
import br.com.fiap.domain.entities.User;
import br.com.fiap.domain.gateways.CryptographyGateway;
import br.com.fiap.domain.gateways.TokenGateway;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

	@Autowired
	private UserDatabaseGateway userDatabaseGateway;

	@Autowired
	private CryptographyGateway cryptographyGateway;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordResetTokenDatabaseGateway passwordResetTokenDatabaseGateway;

	@Autowired
	private TokenGateway tokenGateway;

	
	@Override
	public boolean verifyPassword(String password, String passwordDatabase) {
		return cryptographyGateway.verifyPassword(password, passwordDatabase);
	}

	@Override
	public User getUserByEmail(String email) {
		return userDatabaseGateway.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o e-mail: " + email));
	}

	@Override
	public User getUserByLogin(String login) {
		return userDatabaseGateway.findByLogin(login)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + login));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDatabaseGateway.findByLogin(username)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + username));
		
		return  new AuthenticatedUser(null, user.getName(), user.getLogin(),
				user.getId(), user.getUserTypesRoles());
	}

	@Override
	public void requestPasswordReset(String email) {
		User user = userDatabaseGateway.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

		String token = tokenGateway.generatePasswordResetToken();

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiration(tokenGateway.calculateExpirationTime(1));

		passwordResetTokenDatabaseGateway.save(resetToken);
		emailService.sendResetToken(user.getEmail(), token);

	}
	
	@Override
	public void resetPassword(String token, String newPassword) {
		PasswordResetToken resetToken = passwordResetTokenDatabaseGateway.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("Token inválido"));

		if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Token expirado");
		}

		User user = resetToken.getUser();
		user.setPassword(cryptographyGateway.encodePassword(newPassword));
		userDatabaseGateway.save(user);

		passwordResetTokenDatabaseGateway.delete(resetToken);
	}
}
