package com.example.cookieBank.service;

import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.dto.client.UpdateClientDTO;
import com.example.cookieBank.repository.AccountRepository;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.ClientEntity;
import com.example.cookieBank.repository.ClientRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ConverterToDtoService converterToDtoService;

    public ClientService(ClientRepository clientRepository, AccountRepository accountRepository, ConverterToDtoService converterToDtoService) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.converterToDtoService = converterToDtoService;
    }

    @Transactional
    public ClientDTO saveClient(CreateClientDTO createClient) {
        ClientEntity client = clientRepository.getClientEntityByPhoneAndName(createClient.name(), createClient.phone());

        if (client == null) {
            AccountEntity account = new AccountEntity(createClient.accountNumber(), createClient.secretPin());
            accountRepository.save(account);

            client = clientRepository.save(
                    new ClientEntity(
                            createClient.name(),
                            createClient.lastName(),
                            createClient.phone(),
                            account
                    )
            );
        } else {
            client.setAlive(true);
            client.getAccount().setAlive(true);
        }

        return converterToDtoService.convertClientToDTO(client);
    }

    @Transactional
    public ClientDTO updateClient(UpdateClientDTO updateClient) {
        ClientEntity client = clientRepository
                .getClientEntityById(updateClient.id());

        if (client == null)
            throw new NoResultException("client with id=" + updateClient.id() + " not found!");

        if (updateClient.name() != null) {
            client.setName(updateClient.name());
        }

        if (updateClient.lastName() != null) {
            client.setLastName(updateClient.lastName());
        }

        if (updateClient.phone() != null) {
            client.setPhone(updateClient.phone());
        }

        return converterToDtoService.convertClientToDTO(client);
    }

    public ClientDTO getClientById(Long id) {
        ClientEntity client = clientRepository.getClientEntityById(id);

        if (client == null)
            throw new NoResultException("client with id=" + id + " not found!");

        return converterToDtoService.convertClientToDTO(client);
    }

    public List<ClientDTO> getAllClient() {
        return clientRepository
                .getAllClientEntity()
                .stream()
                .map(converterToDtoService::convertClientToDTO)
                .toList();
    }

    @Transactional
    public void deleteClientById(Long id) {
        ClientEntity client = clientRepository.getClientEntityById(id);

        if (client == null)
            throw new NoResultException("client with id=" + id + " not found!");

        if (!client.getAlive())
            throw new IllegalArgumentException("the client was deleted earlier");

        client.setAlive(false);
        client.getAccount().setAlive(false);
    }
}
