package com.example.cookieBank.controller;

import com.example.cookieBank.dto.auth.AuthDTO;
import com.example.cookieBank.dto.auth.LoginDTO;
import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientDTO> registerClient(
            @Valid @RequestBody CreateClientDTO createClient
    ) {
        ClientDTO client = authorizationService.registerClient(createClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> loginClient(
            @Valid @RequestBody LoginDTO login
    ) {
        AuthDTO auth = authorizationService.loginClient(login);
        return ResponseEntity.status(HttpStatus.OK).body(auth);
    }
    
}
