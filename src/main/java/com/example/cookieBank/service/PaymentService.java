package com.example.cookieBank.service;

import com.example.cookieBank.dto.payment.PaymentDTO;
import com.example.cookieBank.dto.payment.CreatePaymentDTO;
import com.example.cookieBank.dto.payment.PaymentStatus;
import com.example.cookieBank.dto.payment.RequestPaymentDTO;
import com.example.cookieBank.dto.transactional.CreateTransactionalDTO;
import com.example.cookieBank.dto.transactional.TransactionalDTO;
import com.example.cookieBank.repository.AccountRepository;
import com.example.cookieBank.repository.entities.AccountEntity;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final AccountRepository accountRepository;
    private final TransactionalService transactionalService;

    public PaymentService(AccountRepository accountRepository, TransactionalService transactionalService) {
        this.accountRepository = accountRepository;
        this.transactionalService = transactionalService;
    }

    public PaymentDTO servicePayment(RequestPaymentDTO requestPayment) {
        return createPayment(
                new CreatePaymentDTO(
                        "SYSTEM-001",
                        requestPayment.toAccountNumber(),
                        requestPayment.sum()
                )
        );
    }

    public PaymentDTO clientPayment(RequestPaymentDTO requestPaymentDTO, String clientNumberAccount) {
        return createPayment(
                new CreatePaymentDTO(
                        clientNumberAccount,
                        requestPaymentDTO.toAccountNumber(),
                        requestPaymentDTO.sum()
                )
        );
    }

    @Transactional
    private PaymentDTO createPayment(CreatePaymentDTO createTransactional) {
        if (createTransactional.withAccountNumber().equals(createTransactional.toAccountNumber()))
            return createTransactionAndPayment(null, null, createTransactional.sum(), PaymentStatus.INCORRECT);

        AccountEntity toAccount;
        AccountEntity withAccount;

        if (createTransactional.withAccountNumber().compareTo(createTransactional.toAccountNumber()) < 0) {
            withAccount = accountRepository
                    .getAccountEntitiesByAccountNumberForUpdate(createTransactional.withAccountNumber())
                    .orElse(null);
            toAccount = accountRepository
                    .getAccountEntitiesByAccountNumberForUpdate(createTransactional.toAccountNumber())
                    .orElse(null);
        } else {
            toAccount = accountRepository
                    .getAccountEntitiesByAccountNumberForUpdate(createTransactional.toAccountNumber())
                    .orElse(null);
            withAccount = accountRepository
                    .getAccountEntitiesByAccountNumberForUpdate(createTransactional.withAccountNumber())
                    .orElse(null);
        }

        if (withAccount == null || toAccount == null)
            return createTransactionAndPayment(withAccount, toAccount, createTransactional.sum(), PaymentStatus.CANCELED);

        if (withAccount.getBalance().compareTo(createTransactional.sum()) < 0)
            return createTransactionAndPayment(withAccount, toAccount, createTransactional.sum(), PaymentStatus.INCORRECT);

        withAccount.setBalance(
                withAccount.getBalance().subtract(createTransactional.sum())
        );

        toAccount.setBalance(
                toAccount.getBalance().add(createTransactional.sum())
        );

        return createTransactionAndPayment(withAccount, toAccount, createTransactional.sum(), PaymentStatus.SUCCESS);
    }

    private PaymentDTO createTransactionAndPayment(
            AccountEntity withAccount,
            AccountEntity toAccount,
            BigDecimal sum,
            PaymentStatus status
    ) {
        TransactionalDTO transaction = transactionalService.createTransactional(
                new CreateTransactionalDTO(
                        withAccount,
                        toAccount,
                        sum,
                        status
                )
        );

        return new PaymentDTO(
                status,
                transaction
        );
    }
}
