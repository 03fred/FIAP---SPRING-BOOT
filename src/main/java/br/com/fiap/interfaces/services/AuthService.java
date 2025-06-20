package br.com.fiap.interfaces.services;

import br.com.fiap.model.User;


public interface AuthService {

	User getUserByEmail(String email);

	User getUserByLogin(String login);

	boolean verifyPassword(String password, String passwordDatabase);

}
