package br.com.fiap.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.fiap.dto.UserTypeDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.interfaces.services.UserTypeService;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements UserTypeService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public RoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	@Transactional
	public void save(UserTypeDTO userTypeDto) {

	    Role role = this.roleRepository.findByName(userTypeDto.role())
	            .orElseThrow(()-> new ResourceNotFoundException("Permissão não encontrada com o nome: " + userTypeDto.role()));

	    User user = userRepository.findByLogin(userTypeDto.login())
	            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userTypeDto.login()));
	    
	    Set<Role> newRoles =  user.getUserTypesRoles();
	    newRoles.add(role);

	    user.setUserTypesRoles(newRoles);

	    this.userRepository.save(user);
	}

	@Transactional
	@Override
    public void removeRoleFromUser(UserTypeDTO userTypeDto) {
	      Role role = this.roleRepository.findByName(userTypeDto.role())
		            .orElseThrow(()-> new ResourceNotFoundException("Permissão não encontrada com o nome: " + userTypeDto.role()));

	    User user = userRepository.findByLogin(userTypeDto.login())
		            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userTypeDto.login()));
		
        user.removeRole(role);
         userRepository.save(user);
    }

}
