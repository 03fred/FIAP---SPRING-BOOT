package br.com.fiap.integrationTests.controller;

import br.com.fiap.interfaces.controllers.AuthController;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.application.useCases.AuthService;
import br.com.fiap.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static br.com.fiap.factory.UserFactory.createUserMock;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIntegrationTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testAuthorizationWithEmail_Success() throws Exception {
        String email = "ana@email.com";
        String password = "senha123";
        String hashedPassword = "hashedSenha";

        User user = createUserMock();

        when(authService.getUserByEmail(email)).thenReturn(user);
        when(authService.verifyPassword(password, hashedPassword)).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identificador\":\"" + email + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue())); // Verifica que o token (string) não é nulo

        verify(authService).getUserByEmail(email);
        verify(authService).verifyPassword(password, hashedPassword);
    }

    @Test
    void testAuthorizationWithLogin_Failure_InvalidPassword() throws Exception {
        String login = "anaLogin";
        String password = "wrongPassword";
        String hashedPassword = "hashedSenha";

        User user = createUserMock();

        when(authService.getUserByLogin(login)).thenReturn(user);
        when(authService.verifyPassword(password, hashedPassword)).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identificador\":\"" + login + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas, não autorizado"));

        verify(authService).getUserByLogin(login);
        verify(authService).verifyPassword(password, hashedPassword);
    }

    @Test
    void testAuthorization_UserNotFound() throws Exception {
        String identificador = "notfound";

        when(authService.getUserByLogin(identificador)).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identificador\":\"" + identificador + "\", \"password\":\"any\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas, não autorizado."));

        verify(authService).getUserByLogin(identificador);
    }
}