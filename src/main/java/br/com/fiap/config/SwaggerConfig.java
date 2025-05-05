package br.com.fiap.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Swagger Challenge FIAP",
                version = "1.0",
                description = "API desenvolvida para o projeto de pós-graduação da FIAP.",
                contact = @Contact(name = "GitHub", url = "https://github.com/03fred/FIAP---SPRING-BOOT")
        )
)
public class SwaggerConfig {

}
