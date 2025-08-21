package br.com.fiap.application.useCases;

import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.fiap.dto.UserTypeDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.domain.gateways.RoleDatabaseGateway;
import br.com.fiap.domain.gateways.UserDatabaseGateway;
import br.com.fiap.application.useCases.UserTypeService;
import br.com.fiap.domain.entities.Role;
import br.com.fiap.domain.entities.User;
import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements UserTypeService {

	private final UserDatabaseGateway userDatabaseGateway;
	private final RoleDatabaseGateway roleDatabaseGateway;

	public RoleServiceImpl(UserDatabaseGateway userDatabaseGateway, RoleDatabaseGateway roleDatabaseGateway) {
		this.userDatabaseGateway = userDatabaseGateway;
		this.roleDatabaseGateway = roleDatabaseGateway;
	}

	@Override
	@Transactional
	public void save(UserTypeDTO userTypeDto) {

	    Role role = this.roleDatabaseGateway.findByName(userTypeDto.role())
	            .orElseThrow(()-> new ResourceNotFoundException("Permissão não encontrada com o nome: " + userTypeDto.role()));

	    User user = userDatabaseGateway.findByLogin(userTypeDto.login())
	            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userTypeDto.login()));
	    
	    Set<Role> newRoles =  user.getUserTypesRoles();
	    newRoles.add(role);

	    user.setUserTypesRoles(newRoles);

	    this.userDatabaseGateway.save(user);
	}

	@Transactional
	@Override
    public void removeRoleFromUser(UserTypeDTO userTypeDto) {
	      Role role = this.roleDatabaseGateway.findByName(userTypeDto.role())
		            .orElseThrow(()-> new ResourceNotFoundException("Permissão não encontrada com o nome: " + userTypeDto.role()));

	    User user = userDatabaseGateway.findByLogin(userTypeDto.login())
		            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userTypeDto.login()));
		
        user.removeRole(role);
         userDatabaseGateway.save(user);
    }

}
