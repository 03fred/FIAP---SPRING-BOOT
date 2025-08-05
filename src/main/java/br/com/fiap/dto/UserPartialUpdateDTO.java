package br.com.fiap.dto;

import jakarta.validation.Valid;

public record UserPartialUpdateDTO(
        String name,
        String email,
        String login,
        @Valid
        AddressDTO address
){}
