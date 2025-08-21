package br.com.fiap.application.useCases;

import br.com.fiap.dto.UserTypeDTO;

public interface UserTypeService {

	void save(UserTypeDTO userType);

	void removeRoleFromUser(UserTypeDTO userTypeDto);
}
