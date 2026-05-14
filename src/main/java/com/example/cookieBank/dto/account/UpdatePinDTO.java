package com.example.cookieBank.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdatePinDTO(
        @NotBlank(message = "oldPin can't must be null or empty!")
        String oldPin,
        @NotBlank(message = "newPin can't must be null or empty!")
        String newPin
) {}
