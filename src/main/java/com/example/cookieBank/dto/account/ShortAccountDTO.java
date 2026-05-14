package com.example.cookieBank.dto.account;

import java.math.BigDecimal;

public record ShortAccountDTO(
        Long id,
        String numberAccount,
        BigDecimal balance
) {}
