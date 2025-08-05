package br.com.fiap.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.UserAuthorizationDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.PasswordResetTokenRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.AuthService;
import br.com.fiap.interfaces.services.EmailService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.PasswordResetToken;
import br.com.fiap.model.User;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	
	@Override
	public boolean verifyPassword(String password, String passwordDatabase) {
		return passwordEncoder.matches(password, passwordDatabase);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o e-mail: " + email));
	}

	@Override
	public User getUserByLogin(String login) {
		return userRepository.findByLogin(login)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + login));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + username));
		
		return  new AuthenticatedUser(null, user.getName(), user.getLogin(),
				user.getId(), user.getUserTypesRoles());
	}

	@Override
	public void requestPasswordReset(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

		String token = UUID.randomUUID().toString();

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiration(LocalDateTime.now().plusHours(1));

		tokenRepository.save(resetToken);
		emailService.sendResetToken(user.getEmail(), token);

	}
	
	  public void resetPassword(String token, UserAuthorizationDTO userAuth) {
	        PasswordResetToken resetToken = tokenRepository.findByToken(token)
	            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

	        if (resetToken.isExpired()) {
	            throw new IllegalArgumentException("Token expirado");
	        }

	        User user = resetToken.getUser();
	        String passwordCrypto = this.passwordEncoder.encode(userAuth.password());
	        user.setPassword(passwordCrypto);
	        userRepository.save(user);

	        tokenRepository.delete(resetToken); 
	    }
}
