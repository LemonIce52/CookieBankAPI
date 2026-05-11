package com.example.cookieBank.service;

import com.example.cookieBank.DTO.client.ClientDTO;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.ClientEntity;
import jakarta.persistence.Persistence;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConverterService {

    public ClientDTO convertClientToDTO(ClientEntity clientEntity) {
        List<String> accountsNumber = new ArrayList<>();

        boolean isLoadedAccounts = Persistence.getPersistenceUtil().isLoaded(clientEntity, "accounts");

        if (isLoadedAccounts && clientEntity.getAccounts() != null) {
            accountsNumber = clientEntity.getAccounts()
                    .stream()
                    .map(AccountEntity::getNumber)
                    .toList();
        }

        return new ClientDTO(
                clientEntity.getId(),
                clientEntity.getName(),
                clientEntity.getLastName(),
                clientEntity.getPhone(),
                accountsNumber
        );
    }
}
