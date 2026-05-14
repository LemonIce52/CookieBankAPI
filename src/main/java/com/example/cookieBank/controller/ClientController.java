package com.example.cookieBank.controller;

import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.dto.client.UpdateClientDTO;
import com.example.cookieBank.dto.payment.PaymentDTO;
import com.example.cookieBank.dto.payment.RequestPaymentDTO;
import com.example.cookieBank.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@Validated
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDTO>> getVoid() {
        List<ClientDTO> listClient = clientService.getAllClient();
        return ResponseEntity.status(HttpStatus.OK).body(listClient);
    }

    @GetMapping("/me")
    public ResponseEntity<ClientDTO> getClientById(
            @AuthenticationPrincipal @PositiveOrZero(message = "id can't must be less zero") Long id
    ) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PutMapping("/update/me")
    public ResponseEntity<ClientDTO> updateClient(
            @Valid @RequestBody UpdateClientDTO updateClient
    ) {
        ClientDTO client = clientService.updateClient(updateClient);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PutMapping("/createPayment/me")
    public PaymentDTO createPayment(
            @Valid @RequestBody RequestPaymentDTO requestPayment,
            @AuthenticationPrincipal Long id
    ) {
        return clientService.createPayment(requestPayment, id);
    }

    @DeleteMapping("/delete/me")
    public void deleteClient(
            @AuthenticationPrincipal @PositiveOrZero(message = "id can't must be less zero") Long id
    ) {
        clientService.deleteClientById(id);
    }
}
