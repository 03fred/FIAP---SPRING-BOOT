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
        security = @SecurityRequirement(name = "bearerAuth") // 👈 Define o uso global do Bearer Token
)
@SecurityScheme(
        name = "bearerAuth", // 👈 Nome referenciado nos @SecurityRequirement dos endpoints
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT", // 👈 Apenas informativo (pode omitir)
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    // Nenhum método necessário
}
