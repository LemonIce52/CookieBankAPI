package com.example.cookieBank.service;

import com.example.cookieBank.dto.account.AccountDTO;
import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.ClientEntity;
import jakarta.persistence.Persistence;
import org.springframework.stereotype.Service;

@Service
public class ConverterToDtoService {

    public ClientDTO convertClientToDTO(ClientEntity clientEntity) {
        String accountsNumber = "None number";

        boolean isLoadedAccount = Persistence.getPersistenceUtil().isLoaded(clientEntity, "account");

        if (isLoadedAccount && clientEntity.getAccount() != null) {
            accountsNumber = clientEntity.getAccount().getNumber();
        }

        return new ClientDTO(
                clientEntity.getId(),
                clientEntity.getName(),
                clientEntity.getLastName(),
                clientEntity.getPhone(),
                accountsNumber
        );
    }

    public AccountDTO convertAccountToDTO(AccountEntity account) {
        ClientDTO client = null;

        boolean isLoadedAccount = Persistence.getPersistenceUtil().isLoaded(account, "client");

        if (isLoadedAccount && account.getClient() != null) {
            client = convertClientToDTO(account.getClient());
        }

        return new AccountDTO(
                account.getId(),
                account.getNumber(),
                account.getBalance(),
                client
        );
    }
}
