package com.example.cookieBank.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RequestPaymentDTO(
        @NotBlank(message = "toAccountNumber can't must be null or empty!")
        String toAccountNumber,
        @NotNull(message = "sum can't must be null!")
        @Positive(message = "sum can't must be less or equals zero!")
        BigDecimal sum
) {
}
