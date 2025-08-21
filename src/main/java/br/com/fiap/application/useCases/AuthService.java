package br.com.fiap.application.useCases;

import br.com.fiap.dto.UserAuthorizationDTO;
import br.com.fiap.domain.entities.User;


public interface AuthService {

	User getUserByEmail(String email);

	User getUserByLogin(String login);

	boolean verifyPassword(String password, String passwordDatabase);

	void requestPasswordReset(String email);
	
	void resetPassword(String token, String newPassword);
}
