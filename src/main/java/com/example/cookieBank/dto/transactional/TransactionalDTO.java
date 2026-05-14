package com.example.cookieBank.dto.transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionalDTO(
        Long id,
        LocalDateTime dateTime,
        BigDecimal sum,
        String withAccountNumber,
        String toAccountNumber
) {}
