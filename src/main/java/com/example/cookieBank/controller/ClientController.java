package com.example.cookieBank.controller;

import com.example.cookieBank.DTO.client.ClientDTO;
import com.example.cookieBank.DTO.client.CreateClientDTO;
import com.example.cookieBank.DTO.client.UpdateClientDTO;
import com.example.cookieBank.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ClientDTO>> getVoid() {
        List<ClientDTO> listClient = clientService.getAllClient();
        return ResponseEntity.status(HttpStatus.OK).body(listClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(
            @PathVariable("id") @PositiveOrZero(message = "id can't must be less zero") Long id
    ) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PostMapping("/create")
    public ResponseEntity<ClientDTO> createClient(
            @Valid @RequestBody CreateClientDTO createClient
    ) {
        ClientDTO client = clientService.saveClient(createClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PutMapping("/update")
    public ResponseEntity<ClientDTO> updateClient(
            @Valid @RequestBody UpdateClientDTO updateClient
    ) {
        ClientDTO client = clientService.updateClient(updateClient);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(
            @PathVariable("id") @PositiveOrZero(message = "id can't must be less zero") Long id
    ) {
        clientService.deleteClientById(id);
    }
}
