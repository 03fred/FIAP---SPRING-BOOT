package br.com.fiap.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank(message = "A rua é obrigatória")
        String street,

        @NotBlank(message = "O número é obrigatório")
        String number,

        @NotBlank(message = "O bairro é obrigatório")
        String neighborhood,

        @NotBlank(message = "A cidade é obrigatória")
        String city,

        @NotBlank(message = "O estado é obrigatório")
        String state,

        @NotBlank(message = "O CEP é obrigatório")
        String zipCode
) { }

