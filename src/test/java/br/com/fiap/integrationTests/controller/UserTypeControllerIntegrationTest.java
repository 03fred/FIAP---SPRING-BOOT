package br.com.fiap.integrationTests.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.dto.UserTypeDTO;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
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
public class UserTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JavaMailSender mailSender;

    private String jwt;
    private String testLogin;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Cria role ADMIN e uma role de teste
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        Role testRole = new Role();
        testRole.setName("USER");
        roleRepository.save(testRole);

        // Cria usuário com role ADMIN
        User user = new User();
        user.setLogin("admin_" + UUID.randomUUID());
        user.setEmail("admin@example.com");
        user.setPassword(passwordEncoder.encode("adminPass"));
        user.setName("Admin User");
        user.setAddress("Rua Teste, 123");
        user.getUserTypesRoles().add(adminRole);
        userRepository.save(user);

        testLogin = "test_user_" + UUID.randomUUID();
        User testUser = new User();
        testUser.setLogin(testLogin);
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("testPass"));
        testUser.setName("Test User");
        testUser.setAddress("Rua Teste 456");
        userRepository.save(testUser);

        jwt = JwtTokenUtil.createToken(user.getLogin(), null);
    }

    @Test
    void givenValidUserTypeDTO_whenSave_thenReturnSuccessMessage() throws Exception {
        UserTypeDTO dto = new UserTypeDTO(testLogin, "ADMIN");

        mockMvc.perform(post("/users_type")
                .header("Authorization", "Bearer " + jwt)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Permissão cadastrada com sucesso!"));

        User updatedUser = userRepository.findByLogin(testLogin).orElseThrow();
        boolean hasRole = updatedUser.getUserTypesRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
        assertTrue(hasRole);
    }

    @Test
    void givenValidUserTypeDTO_whenDelete_thenReturnSuccessMessage() throws Exception {
        // Adiciona a role MANAGER ao usuário previamente
        Role role = roleRepository.findByName("ADMIN").orElseThrow();
        User user = userRepository.findByLogin(testLogin).orElseThrow();
        user.getUserTypesRoles().add(role);
        userRepository.save(user);

        UserTypeDTO dto = new UserTypeDTO(testLogin, "ADMIN");

        mockMvc.perform(delete("/users_type")
                .header("Authorization", "Bearer " + jwt)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.mensagem").value("Permissão removida com sucesso!"));

        User updatedUser = userRepository.findByLogin(testLogin).orElseThrow();
        boolean stillHasRole = updatedUser.getUserTypesRoles()
                .stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        assertFalse(stillHasRole);
    }
}
