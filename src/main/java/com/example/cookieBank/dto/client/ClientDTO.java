package com.example.cookieBank.dto.client;

public record ClientDTO(
        Long id,
        String name,
        String lastName,
        String phone,
        String accountNumber
) {}
