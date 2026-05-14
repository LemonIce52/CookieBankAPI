package com.example.cookieBank.controller;

import com.example.cookieBank.dto.payment.PaymentDTO;
import com.example.cookieBank.dto.payment.RequestPaymentDTO;
import com.example.cookieBank.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PaymentService paymentService;

    public AdminController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PutMapping("/paymentToClient")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentDTO paymentToClient(
            @Valid @RequestBody RequestPaymentDTO payment
    ) {
        return paymentService.servicePayment(payment);
    }
}
