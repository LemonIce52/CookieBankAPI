package com.example.cookieBank.dto.account;

import com.example.cookieBank.dto.client.ShortClientDTO;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        String number,
        BigDecimal balance,
        ShortClientDTO client
) {}
