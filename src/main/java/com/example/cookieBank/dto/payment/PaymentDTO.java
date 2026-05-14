package com.example.cookieBank.dto.payment;

import com.example.cookieBank.dto.transactional.TransactionalDTO;

public record PaymentDTO(
        PaymentStatus status,
        TransactionalDTO transactional
) {
}
