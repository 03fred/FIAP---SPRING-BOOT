package br.com.fiap.integrationTests.controller;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop" // Ensures H2 schema is created for tests
})
@ActiveProfiles("test") // <-- AQUI
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Long createdUserId;

    // Java
    @BeforeEach
    public void setupTestUser() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext).apply(springSecurity())
                .build();

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole = roleRepository.save(adminRole);

        User user = new User();
        user.setEmail("original@email.com");
        user.setPassword("originalPass123");
        user.setName("Original User");
        user.setAddress("123 Main St");
        user.setLogin("admin_" + UUID.randomUUID()); // <- aqui
        user.getUserTypesRoles().add(adminRole);

        User savedUser = userRepository.save(user);
        createdUserId = savedUser.getId();
    }



    @Test
    public void givenValidUserDTO_whenCreateUser_thenReturnsSuccessMessage() throws Exception {
        UserDTO userDto = new UserDTO(
                "ana@email.com",
                "password123",
                "Ana",
                "123 Main St",
                "ana_login");

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto))
            ).andExpect(status().isCreated())
             .andExpect(jsonPath("$.mensagem").value("Usuário cadastrado com sucesso!"));

        User savedUser = userRepository.findByEmail("ana@email.com").orElseThrow();
        assertEquals("Ana", savedUser.getName());
    }

    @Test
    public void givenInvalidEmail_whenCreateUser_thenReturnsBadRequestStatus() throws Exception {
        UserDTO userDto = new UserDTO(
                "invalid-email",
                "password123",
                "Ana",
                "123 Main St",
                "ana_login");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors", hasItem("email : E-mail inválido")));

        assertFalse(userRepository.findByEmail("invalid-email").isPresent());
    }

    @Test
    public void givenEmptyUserDTO_whenCreateUser_thenReturnsBadRequestStatus() throws Exception {
        userRepository.deleteAll();

        UserDTO userDto = new UserDTO("", "", "", "", "");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isBadRequest());

        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    public void givenNullUserDTO_whenCreateUser_thenReturnsBadRequestStatus() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Explicit username and roles
    @Transactional
    public void givenValidUserDTO_whenUpdateUserWithAdmin_thenReturnsOkStatus() throws Exception {
        // Java
        UserUpdateDTO userUpdateDto = new UserUpdateDTO(
                "ana@email.com",
                "Ana",
                "123 Main St",
                "ana_login");

        mockMvc.perform(put("/users/update/" + createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Usuário atualizado com sucesso."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAccessProtectedEndpoint() throws Exception {
        mockMvc.perform(put("/users/update/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }




}
