package br.com.fiap.config;

import java.util.HashSet;
import java.util.Set;

import br.com.fiap.model.Address;
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
    @Transactional 
    public void run(String... args) throws Exception {
    	
			if (userRepository.findByLogin("admin").isEmpty()) {
				User adminUser = new User();
				adminUser.setName("admin");
				adminUser.setEmail("admin@admin.com.br");
				adminUser.setLogin("admin");
				Address adminAddress = new Address(
						null,
						"Rua Admin",
						"123",
						"Centro",
						"São Paulo",
						"SP",
						"01000-000",
						adminUser
				);
				adminUser.setPassword(passwordEncoder.encode("123"));

				Role roleAdmin = roleRepository.findByName(EnumUserType.ADMIN.toString())
						.orElseGet(() -> roleRepository.save(new Role(EnumUserType.ADMIN.toString())));
				
				Role roleUser = roleRepository.findByName(EnumUserType.USER.toString())
						.orElseGet(() -> roleRepository.save(new Role(EnumUserType.USER.toString())));
				
				Role roleRestauranteOwner = roleRepository.findByName(EnumUserType.RESTAURANT_OWNER.toString())
						.orElseGet(() -> roleRepository.save(new Role(EnumUserType.RESTAURANT_OWNER.toString())));
			
				Set<Role> userTypesRoles = new HashSet<>();
				userTypesRoles.add(roleAdmin);
				userTypesRoles.add(roleUser);
				userTypesRoles.add(roleRestauranteOwner);
						
				adminUser.setUserTypesRoles(userTypesRoles);

				var save = userRepository.save(adminUser);

				System.out.println("Admin user created successfully!");
			} else {
				System.out.println("Admin user already exists.");
			}
	
    }
}