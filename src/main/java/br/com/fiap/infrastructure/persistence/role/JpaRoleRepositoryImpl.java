package br.com.fiap.infrastructure.persistence.role;

import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.Role;
import br.com.fiap.gateways.RoleRepository;
import br.com.fiap.mapper.RoleMapper;

@Repository
public class JpaRoleRepositoryImpl implements RoleRepository {
	JpaRoleRepository jpaRepo;
	
    public JpaRoleRepositoryImpl(JpaRoleRepository jpaRepo) {
       this.jpaRepo = jpaRepo;
    }

	@Override
	public Role save(Role role) {
		return RoleMapper.toDomain(jpaRepo.save(RoleMapper.toEntity(role)));
	}

	@Override
	public void delete(Role role) {
		jpaRepo.deleteById(role.getId());

	}
}