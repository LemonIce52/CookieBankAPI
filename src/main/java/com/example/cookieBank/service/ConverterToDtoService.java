package com.example.cookieBank.service;

import com.example.cookieBank.dto.account.AccountDTO;
import com.example.cookieBank.dto.account.ShortAccountDTO;
import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.ShortClientDTO;
import com.example.cookieBank.dto.transactional.TransactionalDTO;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.client.ClientEntity;
import com.example.cookieBank.repository.entities.TransactionalEntity;
import com.example.cookieBank.repository.entities.client.CompanyClientEntity;
import com.example.cookieBank.repository.entities.client.IndividualClientEntity;
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

        if (clientEntity instanceof IndividualClientEntity individualClient) {
            return new ClientDTO(
                    individualClient.getId(),
                    individualClient.getName(),
                    individualClient.getLastName(),
                    null,
                    individualClient.getPhone(),
                    account
            );
        } else if (clientEntity instanceof CompanyClientEntity companyClient) {
            return new ClientDTO(
                    companyClient.getId(),
                    null,
                    null,
                    companyClient.getCompanyName(),
                    companyClient.getPhone(),
                    account
            );
        } else {
            return new ClientDTO(
                    clientEntity.getId(),
                    null,
                    null,
                    null,
                    clientEntity.getPhone(),
                    account
            );
        }
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
        if (client instanceof IndividualClientEntity individualClient) {
            return new ShortClientDTO(
                    individualClient.getId(),
                    individualClient.getName(),
                    individualClient.getLastName(),
                    null,
                    individualClient.getPhone()
            );
        } else if (client instanceof CompanyClientEntity companyClient) {
            return new ShortClientDTO(
                    companyClient.getId(),
                    null,
                    null,
                    companyClient.getCompanyName(),
                    companyClient.getPhone()
            );
        } else {
            return new ShortClientDTO(
                    client.getId(),
                    null,
                    null,
                    null,
                    client.getPhone()
            );
        }
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
