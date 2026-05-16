package com.example.cookieBank.dto.client;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateClientDTO(
        @NotNull(message = "id can' must be null!")
        @PositiveOrZero(message = "id can't must be less zero")
        Long id,
        @Pattern(regexp = "^(?!\\s*$).+", message = "name can't must be empty!")
        String name,
        @Pattern(regexp = "^(?!\\s*$).+", message = "lastName can't must be empty!")
        String lastName,
        @Pattern(regexp = "^(?!\\s*$).+", message = "companyName can't must be empty!")
        String companyName,
        @Pattern(regexp = "^(?!\\s*$).+", message = "phone can't must be empty!")
        String phone
) {
}
