package br.com.fiap.integrationTests.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import br.com.fiap.dto.*;
import br.com.fiap.factory.AddressFactory;
import br.com.fiap.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop" // Ensures H2 schema is created for tests
})
@ActiveProfiles("test") // <-- AQUI
@Transactional
public class UserControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Long createdUserId;

    @Autowired
    private PasswordEncoder passwordEncoder;

    String jwt;


    @BeforeEach
	public void setupTestUser() {
		
	    userRepository.deleteAll();
	    roleRepository.deleteAll();
	
	    Role adminRole = new Role();
	    adminRole.setName("ADMIN");
	    adminRole = roleRepository.save(adminRole);
	
	    User user = new User();
	    user.setEmail("original@email.com");
	    user.setPassword(passwordEncoder.encode("originalPass123"));
	    user.setName("Original User");
        user.setAddress(AddressFactory.getMockAddress(user));
        user.setLogin("admin_" + UUID.randomUUID());
        user.getUserTypesRoles().add(adminRole);

        User savedUser = userRepository.save(user);
	    createdUserId = savedUser.getId();
        jwt = JwtTokenUtil.createToken(user.getLogin(), null); 

	}


    @Test
    public void givenValidUserDTO_whenCreateUser_thenReturnsSuccessMessage() throws Exception {
        UserDTO userDto = new UserDTO(
                "ana@email.com",
                "password123",
                "Ana",
                AddressFactory.getMockAddressDTO(),
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
                new AddressDTO(
                        "Rua Exemplo",  // street
                        "123",          // number
                        "Bairro Legal", // neighborhood
                        "São Paulo",    // city
                        "SP",           // state
                        "01000-000"     // zipCode
                ),
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

        UserDTO userDto = new UserDTO("", "", "", new AddressDTO("", "", "", "", "", ""), "");

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
                AddressFactory.getMockAddressDTO(),
                "ana_login");

        mockMvc.perform(put("/users/update/" + createdUserId)
                		.header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenValidUserId_whenDeleteUser_thenReturnsOkStatus() throws Exception {
        mockMvc.perform(
                    delete("/users/delete/" + createdUserId)
                    .header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findById(createdUserId).isPresent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenValidUserId_whenGetUserById_thenReturnsOkStatus() throws Exception {

        mockMvc.perform(
                get("/users/detail/" + createdUserId)
                		.header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertTrue(userRepository.findById(createdUserId).isPresent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdminUser_whenRequestAllUsers_thenReturnsUsersAndOkStatus() throws Exception {
        mockMvc.perform(get("/users/all")
                		.header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].email").value("original@email.com"))
                .andExpect(jsonPath("$.content[0].name").value("Original User"));

        // Optional DB check if needed
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdminUser_whenUpdatePartialUser_thenReturnsOkStatus() throws Exception {
        UserPartialUpdateDTO userPartialUpdateDTO = new UserPartialUpdateDTO(
                "Updated user",
                "updated@email.com",
                "456 New St",
                AddressFactory.getMockAddressDTO()
        );

        mockMvc.perform(
                patch("/users/update-partial/" + createdUserId)
                		.header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPartialUpdateDTO))
            )
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdminUser_whenUpdatePassword_thenReturnsOkStatus() throws Exception {
        PasswordUpdateDTO newPassword = new PasswordUpdateDTO(
                "originalPass123",
                "newPassword123",
                "newPassword123"
        );

        mockMvc.perform(
                        patch("/users/update-password/" + createdUserId)
                        		.header("Authorization", "Bearer " + jwt)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newPassword))
                )
        	.andExpect(status().isNoContent());
    }
}
