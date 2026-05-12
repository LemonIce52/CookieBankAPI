package com.example.cookieBank.dto.account;

import com.example.cookieBank.dto.client.ClientDTO;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        String number,
        BigDecimal balance,
        ClientDTO client
) {}
