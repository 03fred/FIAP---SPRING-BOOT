package br.com.fiap.services;

import org.springframework.stereotype.Service;

import br.com.fiap.dto.UserTypeDTO;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.services.UserTypeService;

@Service
public class RoleServiceImpl implements UserTypeService {

	private final RoleRepository repository;

	public RoleServiceImpl(RoleRepository repository) {
		this.repository = repository;
	}

	@Override
	public void save(UserTypeDTO userTypeDto) {
		

	}

}
