package br.com.fiap.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Swagger Challenge FIAP",
                version = "1.0",
                description = "API desenvolvida para o projeto de pós-graduação da FIAP.",
                contact = @Contact(name = "GitHub", url = "https://github.com/03fred/FIAP---SPRING-BOOT")
        ),
        security = @SecurityRequirement(name = "bearerAuth") 
)
@SecurityScheme(
        name = "bearerAuth", 
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    // Nenhum método necessário
}
