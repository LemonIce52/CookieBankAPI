package com.example.cookieBank.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "phone can't must be null or empty!")
        String phone,
        @NotBlank(message = "secretPin can't must be null or empty!")
        String secretPin
) {}
