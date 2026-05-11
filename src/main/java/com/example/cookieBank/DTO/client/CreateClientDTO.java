package com.example.cookieBank.DTO.client;

import jakarta.validation.constraints.NotBlank;

public record CreateClientDTO(
        @NotBlank(message = "name can't must be null or empty!")
        String name,
        @NotBlank(message = "lastName can't must be null or empty!")
        String lastName,
        @NotBlank(message = "phone can't must be null or empty!")
        String phone
) {}
