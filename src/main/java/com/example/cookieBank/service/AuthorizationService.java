package com.example.cookieBank.service;

import com.example.cookieBank.component.JwtProvider;
import com.example.cookieBank.dto.auth.AuthDTO;
import com.example.cookieBank.dto.auth.LoginDTO;
import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.repository.ClientRepository;
import com.example.cookieBank.repository.entities.ClientEntity;
import jakarta.persistence.NoResultException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthorizationService(
            ClientService clientService,
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public ClientDTO registerClient(CreateClientDTO createClient) {
        return clientService.saveClient(createClient);
    }


    public AuthDTO loginClient(LoginDTO login) {
        ClientEntity client = clientRepository.getClientEntityByPhone(login.phone())
                .orElseThrow(() -> new NoResultException("User bot found!"));

        if (!passwordEncoder.matches(login.secretPin(), client.getAccount().getAccountAccessPin())) {
            throw new IllegalArgumentException("Incorrect data!");
        }

        String token = jwtProvider.generateToken(client.getId(), client.getRole().name());

        return new AuthDTO(token);
    }
}
