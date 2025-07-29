package br.com.fiap.integrationTests.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
@Transactional
public class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JavaMailSender mailSender;

    private String jwtAdmin;
    private Long ownerId;

    @BeforeEach
    void setup() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        Role ownerRole = new Role();
        ownerRole.setName("RESTAURANT_OWNER");
        roleRepository.save(ownerRole);

        // Usuário ADMIN autenticado
        User admin = new User();
        admin.setLogin("admin_" + UUID.randomUUID());
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setName("Admin");
        admin.setAddress("Admin Street");
        admin.getUserTypesRoles().add(adminRole);
        userRepository.save(admin);

        jwtAdmin = JwtTokenUtil.createToken(admin.getLogin(), null);

        // Usuário dono do restaurante
        User owner = new User();
        owner.setLogin("owner_" + UUID.randomUUID());
        owner.setEmail("owner@test.com");
        owner.setPassword(passwordEncoder.encode("owner123"));
        owner.setName("Owner");
        owner.setAddress("Owner Street");
        owner.getUserTypesRoles().add(ownerRole);
        ownerId = userRepository.save(owner).getId();
    }

    @Test
    void givenValidRestaurantDTO_whenAdminCreatesRestaurant_thenStatusCreated() throws Exception {
        RestaurantDTO dto = new RestaurantDTO(
                "Restaurante Teste",
                "Rua das Flores",
                "BRASILEIRA",
                LocalTime.of(10, 0),
                LocalTime.of(22, 0)
        );

        mockMvc.perform(post("/restaurant/user/owner/" + ownerId)
                .header("Authorization", "Bearer " + jwtAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(1, restaurants.size());
        assertEquals("Restaurante Teste", restaurants.get(0).getName());
        assertEquals(ownerId, restaurants.get(0).getRestaurantOwner().getId());
    }
}