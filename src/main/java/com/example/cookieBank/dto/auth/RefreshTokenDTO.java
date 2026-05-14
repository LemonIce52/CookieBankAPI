package com.example.cookieBank.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(
        @NotBlank(message = "refreshToken can't must be null or empty!")
        String refreshToken
) {}
