package br.com.fiap.config;

import org.springframework.stereotype.Component;

@Component
public class DataLoader /*implements CommandLineRunner*/ {

	/*JpaUserRepository userRepository;
	PasswordEncoder passwordEncoder;
	RoleRepository roleRepository;
	
	public DataLoader(JpaUserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
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
                adminUser.setAddress(adminAddress);
                
				var save = userRepository.save(adminUser);

				System.out.println("Admin user created successfully!");
			} else {
				System.out.println("Admin user already exists.");
			}
	
    }*/
}