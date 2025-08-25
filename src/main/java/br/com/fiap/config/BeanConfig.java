package br.com.fiap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.application.useCases.users.CreateUserUseCase;
import br.com.fiap.gateways.UserRepository;

@Configuration
public class BeanConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository repo) {
        return new CreateUserUseCase(repo);
    }

}