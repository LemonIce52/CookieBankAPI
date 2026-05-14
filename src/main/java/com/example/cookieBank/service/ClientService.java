package com.example.cookieBank.service;

import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.dto.client.UpdateClientDTO;
import com.example.cookieBank.dto.payment.PaymentDTO;
import com.example.cookieBank.dto.payment.RequestPaymentDTO;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.ClientEntity;
import com.example.cookieBank.repository.ClientRepository;
import com.example.cookieBank.repository.entities.RoleClients;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ConverterToDtoService converterToDtoService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;

    public ClientService(ClientRepository clientRepository, ConverterToDtoService converterToDtoService, PasswordEncoder passwordEncoder, PaymentService paymentService) {
        this.clientRepository = clientRepository;
        this.converterToDtoService = converterToDtoService;
        this.passwordEncoder = passwordEncoder;
        this.paymentService = paymentService;
    }

    @Transactional
    public ClientDTO saveClient(CreateClientDTO createClient) {
        if (createClient.name().equalsIgnoreCase("bank"))
            throw new IllegalArgumentException("This name is reserved!");

        if (createClient.accountNumber().toLowerCase(Locale.ROOT).startsWith("system-"))
            throw new IllegalArgumentException("Account number can't starts with 'SYSTEM-'");

        return clientRepository.getDontAliveClientEntityByPhone(createClient.phone())
                .map(clientEntity -> {

                    if (!clientEntity.getName().equals(createClient.name()))
                        throw new IllegalArgumentException("This phone number is already registered to another person!");

                    clientEntity.setAlive(true);
                    if (clientEntity.getAccount() != null)
                        clientEntity.getAccount().setAlive(true);

                    return converterToDtoService.convertClientToDTO(clientEntity);
                })
                .orElseGet(() -> {
                    String pin = passwordEncoder.encode(createClient.secretPin());
                    AccountEntity account = new AccountEntity(createClient.accountNumber(), pin);

                    ClientEntity newClient = clientRepository.save(
                            new ClientEntity(
                                    createClient.name(),
                                    createClient.lastName(),
                                    createClient.phone(),
                                    RoleClients.CLIENT,
                                    account
                            )
                    );
                    return converterToDtoService.convertClientToDTO(newClient);
                });
    }

    @Transactional
    public ClientDTO updateClient(UpdateClientDTO updateClient) {
        ClientEntity client = clientRepository
                .getClientEntityById(updateClient.id())
                .orElseThrow(() -> new NoResultException("client with id=" + updateClient.id() + " not found!"));

        if (updateClient.name() != null) {
            if (updateClient.name().equalsIgnoreCase("bank"))
                throw new IllegalArgumentException("This name is reserved!");

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
        ClientEntity client = clientRepository
                                .getClientEntityById(id)
                                .orElseThrow(() -> new NoResultException("client with id=" + id + " not found!"));

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
        ClientEntity client = clientRepository
                                .getClientEntityById(id)
                                .orElseThrow(() -> new NoResultException("client with id=" + id + " not found!"));

        if (!client.getAlive())
            throw new IllegalArgumentException("the client was deleted earlier");

        client.setAlive(false);
        client.getAccount().setAlive(false);
    }

    public PaymentDTO createPayment(RequestPaymentDTO requestPayment, Long id) {
        ClientEntity client = clientRepository
                                .getClientEntityById(id)
                                .orElseThrow(() -> new NoResultException("client with id=" + id + " not found!"));

        if (client.getAccount() == null)
            throw new IllegalStateException("Account not fount on client with id=" + id);

        return paymentService.clientPayment(requestPayment, client.getAccount().getNumber());
    }
}
