package br.com.fiap.interfaces.services;

import br.com.fiap.dto.UserTypeDTO;

public interface UserTypeService {

	void save(UserTypeDTO userType);

	void removeRoleFromUser(UserTypeDTO userTypeDto);
}
