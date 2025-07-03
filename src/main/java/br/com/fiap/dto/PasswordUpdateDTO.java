package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDTO(
        @NotNull(message = "A senha atual precisa ser informada.")
        @Size(min = 6, message = "A senha atual deve ter no mínimo 6 caracteres.")
        String oldPassword,

        @NotNull(message = "A nova senha precisa ser informada.")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "A nova senha deve conter letras e números."
        )
        String newPassword,

        @NotNull(message = "A confirmação da nova senha precisa ser informada.")
        @Size(min = 6, message = "A confirmação da senha deve ter no mínimo 6 caracteres.")
        String confirmPassword
){}

