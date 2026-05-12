package com.example.cookieBank.service;

import com.example.cookieBank.dto.account.AccountDTO;
import com.example.cookieBank.dto.account.UpdatePinDTO;
import com.example.cookieBank.repository.AccountRepository;
import com.example.cookieBank.repository.entities.AccountEntity;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ConverterToDtoService converterToDtoService;

    public AccountService(AccountRepository accountRepository, ConverterToDtoService converterToDtoService) {
        this.accountRepository = accountRepository;
        this.converterToDtoService = converterToDtoService;
    }

    public AccountDTO getAccountById(Long id) {
        AccountEntity account = accountRepository.getAccountEntitiesById(id);

        if (account == null)
            throw new NoResultException("Account with id=" + id + " not found!");

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
    public void updatePin(UpdatePinDTO updatePin) {
        AccountEntity account = accountRepository.getAccountEntitiesById(updatePin.id());

        if (account == null)
            throw new NoResultException("Account with id=" + updatePin.id() + " not found!");

        if (!account.getAccountAccessPin().equals(updatePin.oldPin()))
            throw new IllegalArgumentException("Old pin is incorrect!");

        if (account.getAccountAccessPin().equals(updatePin.newPin()))
            throw new IllegalArgumentException("The new pin should not match the old one!");

        account.setAccountAccessPin(updatePin.newPin());
    }
}
