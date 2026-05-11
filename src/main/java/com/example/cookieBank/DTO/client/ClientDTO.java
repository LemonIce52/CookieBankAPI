package com.example.cookieBank.DTO.client;

import java.util.List;

public record ClientDTO(
        Long id,
        String name,
        String lastName,
        String phone,
        List<String> accountsNumber
) {}
