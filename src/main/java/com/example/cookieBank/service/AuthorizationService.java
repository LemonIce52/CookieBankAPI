package com.example.cookieBank.service;

import com.example.cookieBank.component.JwtProvider;
import com.example.cookieBank.dto.auth.AuthDTO;
import com.example.cookieBank.dto.auth.LoginDTO;
import com.example.cookieBank.dto.auth.RefreshTokenDTO;
import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.repository.ClientRepository;
import com.example.cookieBank.repository.TokenRepository;
import com.example.cookieBank.repository.entities.ClientEntity;
import com.example.cookieBank.repository.entities.TokensEntity;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthorizationService(
            ClientService clientService,
            ClientRepository clientRepository, TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public ClientDTO registerClient(CreateClientDTO createClient) {
        return clientService.saveClient(createClient);
    }


    @Transactional
    public AuthDTO loginClient(LoginDTO login) {
        ClientEntity client = clientRepository.getClientEntityByPhone(login.phone())
                .orElseThrow(() -> new NoResultException("User bot found!"));

        if (!client.getAlive())
            throw new NoResultException("User bot found!");

        if (!passwordEncoder.matches(login.secretPin(), client.getAccount().getAccountAccessPin())) {
            throw new IllegalArgumentException("Incorrect data!");
        }

        AuthDTO tokens = getTokens(client);

        TokensEntity tokenEntity = tokenRepository.getTokensEntitiesByClientId(client.getId()).orElse(null);

        if (tokenEntity == null) {
            tokenRepository.save(
                    new TokensEntity(
                            client,
                            tokens.refreshToken()
                    )
            );
        } else {
            tokenRepository.updateRefreshTokenById(tokens.refreshToken(), tokenEntity.getId());
        }

        return tokens;
    }

    @Transactional
    public AuthDTO refreshToken(RefreshTokenDTO refreshToken) {
        if (!jwtProvider.validatedToken(refreshToken.refreshToken())) {
            throw new IllegalStateException("Invalid token!");
        }

        Long clientId = jwtProvider.getClientIdFromToken(refreshToken.refreshToken());

        TokensEntity token = tokenRepository
                                .getTokensEntitiesByClientId(clientId)
                                .orElseThrow(() -> new NoResultException("Token not found!"));

        if (!token.getRefreshToken().equals(refreshToken.refreshToken()))
            throw new IllegalArgumentException("Invalid token");

        AuthDTO tokens = getTokens(token.getClient());

        tokenRepository.updateRefreshTokenById(tokens.refreshToken(), token.getId());

        return tokens;
    }

    private AuthDTO getTokens(ClientEntity client) {
        String jwtToken = jwtProvider.generateToken(client.getId(), client.getRole().name());
        String refreshJwtToken = jwtProvider.generateRefreshToken(client.getId());
        return new AuthDTO(jwtToken, refreshJwtToken);
    }
}
