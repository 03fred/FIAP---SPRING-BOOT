package br.com.fiap.dto;

public record UserPartialUpdateDTO(
        String name,
        String email,
        String address,
        String login
){}
