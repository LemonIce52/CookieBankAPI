package com.example.cookieBank.service;

import com.example.cookieBank.dto.account.AccountDTO;
import com.example.cookieBank.dto.account.ShortAccountDTO;
import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.ShortClientDTO;
import com.example.cookieBank.dto.transactional.TransactionalDTO;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.ClientEntity;
import com.example.cookieBank.repository.entities.TransactionalEntity;
import jakarta.persistence.Persistence;
import org.springframework.stereotype.Service;

@Service
public class ConverterToDtoService {

    public ClientDTO convertClientToDTO(ClientEntity clientEntity) {
        ShortAccountDTO account = null;

        boolean isLoadedAccount = Persistence.getPersistenceUtil().isLoaded(clientEntity, "account");

        if (isLoadedAccount && clientEntity.getAccount() != null) {
            account = convertAccountToShortDTO(clientEntity.getAccount());
        }

        return new ClientDTO(
                clientEntity.getId(),
                clientEntity.getName(),
                clientEntity.getLastName(),
                clientEntity.getPhone(),
                account
        );
    }

    public AccountDTO convertAccountToDTO(AccountEntity account) {
        ShortClientDTO client = null;

        boolean isLoadedClient = Persistence.getPersistenceUtil().isLoaded(account, "client");

        if (isLoadedClient && account.getClient() != null) {
            client = convertClientToShortDTO(account.getClient());
        }

        return new AccountDTO(
                account.getId(),
                account.getNumber(),
                account.getBalance(),
                client
        );
    }

    public ShortAccountDTO convertAccountToShortDTO(AccountEntity account) {
        return new ShortAccountDTO(
                account.getId(),
                account.getNumber(),
                account.getBalance()
        );
    }

    public ShortClientDTO convertClientToShortDTO(ClientEntity client) {
        return new ShortClientDTO(
                client.getId(),
                client.getName(),
                client.getLastName(),
                client.getPhone()
        );
    }

    public TransactionalDTO convertTransactionalToDTO(TransactionalEntity transactional) {
        String toAccountNumber = "Empty";
        String withAccountNumber = "Empty";

        boolean isLoadedToAccount = Persistence.getPersistenceUtil().isLoaded(transactional, "toAccount");
        boolean isLoadedWithAccount = Persistence.getPersistenceUtil().isLoaded(transactional, "withAccount");

        if (isLoadedToAccount && transactional.getToAccount() != null) {
            toAccountNumber = transactional.getToAccount().getNumber();
        }

        if (isLoadedWithAccount && transactional.getWithAccount() != null) {
            withAccountNumber = transactional.getWithAccount().getNumber();
        }

        return new TransactionalDTO(
                transactional.getId(),
                transactional.getDateTime(),
                transactional.getSumTransaction(),
                withAccountNumber,
                toAccountNumber,
                transactional.getPaymentStatus()
        );
    }
}
