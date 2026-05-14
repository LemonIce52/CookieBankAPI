package com.example.cookieBank.dto.client;

import com.example.cookieBank.dto.account.ShortAccountDTO;

public record ClientDTO(
        Long id,
        String name,
        String lastName,
        String phone,
        ShortAccountDTO account
) {}
