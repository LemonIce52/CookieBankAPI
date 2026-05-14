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
        AccountEntity withAccount = accountRepository
                .getAccountEntitiesByAccountNumber(createTransactional.withAccountNumber())
                .orElseThrow(() -> new NoResultException("Account with number " + createTransactional.withAccountNumber() + " not found!"));

        if (withAccount.getBalance().compareTo(createTransactional.sum()) < 0)
            throw new IllegalArgumentException("Balance is less sum!");

        AccountEntity toAccount = accountRepository
                .getAccountEntitiesByAccountNumber(createTransactional.toAccountNumber())
                .orElseThrow(() -> new NoResultException("Account with number " + createTransactional.toAccountNumber() + " not found!"));

        withAccount.setBalance(
                withAccount.getBalance().subtract(createTransactional.sum())
        );

        toAccount.setBalance(
                toAccount.getBalance().add(createTransactional.sum())
        );

        TransactionalDTO transaction = transactionalService.createTransactional(
                new CreateTransactionalDTO(
                        withAccount,
                        toAccount,
                        createTransactional.sum()
                )
        );

        return new PaymentDTO(
                PaymentStatus.SUCCESS,
                transaction
        );
    }
}
