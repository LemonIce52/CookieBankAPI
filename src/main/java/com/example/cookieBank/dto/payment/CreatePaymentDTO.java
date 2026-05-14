package com.example.cookieBank.dto.payment;

import java.math.BigDecimal;

public record CreatePaymentDTO(
        String withAccountNumber,
        String toAccountNumber,
        BigDecimal sum
) {}
