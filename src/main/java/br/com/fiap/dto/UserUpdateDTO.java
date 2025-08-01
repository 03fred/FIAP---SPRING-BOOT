package br.com.fiap.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserUpdateDTO(

        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,

        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Valid @NotNull(message = "O endereço é obrigatório")
        AddressDTO address,

        @NotBlank(message = "O login é obrigatório")
        @Pattern(regexp = "^[^@]+$", message = "O login não pode conter '@'")
        String login

){}
