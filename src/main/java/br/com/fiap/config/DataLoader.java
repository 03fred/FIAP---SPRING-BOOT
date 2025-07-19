package br.com.fiap.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // Importante!

import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import br.com.fiap.model.enums.EnumUserType;

@Component
public class DataLoader implements CommandLineRunner {

	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	RoleRepository roleRepository;
	
	public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

    @Override
    @Transactional // Garante que esta operação esteja dentro de uma transação
    public void run(String... args) throws Exception {
    
			// Check if an admin user already exists to prevent duplicates on restart
			if (userRepository.findByLogin("admin").isEmpty()) {
				User adminUser = new User();
				adminUser.setName("admin");
				adminUser.setEmail("admin@admin.com.br");
				adminUser.setLogin("admin");
				adminUser.setAddress("Rua - admin 123");
				adminUser.setPassword(passwordEncoder.encode("123"));

				Role role = roleRepository.findByName(EnumUserType.ADMIN.toString())
						.orElseGet(() -> roleRepository.save(new Role(EnumUserType.ADMIN.toString())));
				adminUser.setUserTypesRoles(Set.of(role));

				var save = userRepository.save(adminUser);

				System.out.println("Admin user created successfully!");
			} else {
				System.out.println("Admin user already exists.");
			}
	
    }
}