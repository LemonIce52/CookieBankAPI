package com.example.cookieBank.service;

import com.example.cookieBank.dto.account.AccountDTO;
import com.example.cookieBank.dto.account.UpdatePinDTO;
import com.example.cookieBank.repository.AccountRepository;
import com.example.cookieBank.repository.entities.AccountEntity;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ConverterToDtoService converterToDtoService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, ConverterToDtoService converterToDtoService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.converterToDtoService = converterToDtoService;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountDTO getAccountByClientId(Long id) {
        AccountEntity account = accountRepository
                                .getAccountEntitiesByClientId(id)
                                .orElseThrow(() -> new NoResultException("Account with id=" + id + " not found!"));

        return converterToDtoService.convertAccountToDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository
                .getAllAccounts()
                .stream()
                .map(converterToDtoService::convertAccountToDTO)
                .toList();
    }

    @Transactional
    public void updatePin(UpdatePinDTO updatePin, Long clientId) {
        AccountEntity account = accountRepository
                                .getAccountEntitiesByClientId(clientId)
                                .orElseThrow(() -> new NoResultException("Account with clientId=" + clientId + " not found!"));

        if (!passwordEncoder.matches(updatePin.oldPin(), account.getAccountAccessPin()))
            throw new IllegalArgumentException("Old pin is incorrect!");

        if (passwordEncoder.matches(updatePin.newPin(), account.getAccountAccessPin()))
            throw new IllegalArgumentException("The new pin should not match the old one!");

        account.setAccountAccessPin(passwordEncoder.encode(updatePin.newPin()));
    }
}
