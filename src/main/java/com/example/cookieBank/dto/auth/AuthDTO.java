package com.example.cookieBank.dto.auth;

public record AuthDTO(
        String token,
        String refreshToken
) {}
