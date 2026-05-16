package com.example.cookieBank.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateClientDTO(
        @Pattern(regexp = "^(?!\\s*$).+", message = "name can't must be empty!")
        String name,
        @Pattern(regexp = "^(?!\\s*$).+", message = "lastName can't must be empty!")
        String lastName,
        @Pattern(regexp = "^(?!\\s*$).+", message = "companyName can't must be empty!")
        String companyName,
        @NotBlank(message = "phone can't must be null or empty!")
        String phone,
        @NotBlank(message = "accountNumber can't must be null or empty!")
        String accountNumber,
        @NotBlank(message = "secretPin can't must be null or empty!")
        String secretPin
) {}
