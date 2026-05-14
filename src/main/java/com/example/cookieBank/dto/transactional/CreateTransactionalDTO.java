package com.example.cookieBank.dto.transactional;

import com.example.cookieBank.dto.payment.PaymentStatus;
import com.example.cookieBank.repository.entities.AccountEntity;

import java.math.BigDecimal;

public record CreateTransactionalDTO(
        AccountEntity withAccount,
        AccountEntity toAccount,
        BigDecimal sum,
        PaymentStatus paymentStatus
) {
}
