package com.example.cookieBank.service;

import com.example.cookieBank.DTO.client.ClientDTO;
import com.example.cookieBank.DTO.client.CreateClientDTO;
import com.example.cookieBank.DTO.client.UpdateClientDTO;
import com.example.cookieBank.repository.entities.ClientEntity;
import com.example.cookieBank.repository.impl.ClientRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ConverterService converterService;

    public ClientService(ClientRepository clientRepository, ConverterService converterService) {
        this.clientRepository = clientRepository;
        this.converterService = converterService;
    }

    public ClientDTO saveClient(CreateClientDTO createClient) {
        ClientEntity savedClient = clientRepository.save(
                new ClientEntity(
                        createClient.name(),
                        createClient.lastName(),
                        createClient.phone()
                )
        );

        return converterService.convertClientToDTO(savedClient);
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

        return converterService.convertClientToDTO(client);
    }

    public ClientDTO getClientById(Long id) {
        ClientEntity client = clientRepository.getClientEntityById(id);

        if (client == null)
            throw new NoResultException("client with id=" + id + " not found!");

        return converterService.convertClientToDTO(client);
    }

    public List<ClientDTO> getAllClient() {
        return clientRepository
                .getAllClientEntity()
                .stream()
                .map(converterService::convertClientToDTO)
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
    }
}
