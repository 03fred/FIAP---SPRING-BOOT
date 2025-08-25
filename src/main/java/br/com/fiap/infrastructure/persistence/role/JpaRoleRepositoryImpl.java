package br.com.fiap.infrastructure.persistence.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRoleRepositoryImpl  {
    
    @Autowired
    private JpaRoleRepository jpaRepo;
    
}