package com.example.cookieBank.service;

import com.example.cookieBank.dto.client.ClientDTO;
import com.example.cookieBank.dto.client.CreateClientDTO;
import com.example.cookieBank.dto.client.UpdateClientDTO;
import com.example.cookieBank.dto.payment.PaymentDTO;
import com.example.cookieBank.dto.payment.RequestPaymentDTO;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.client.ClientEntity;
import com.example.cookieBank.repository.ClientRepository;
import com.example.cookieBank.repository.entities.RoleClients;
import com.example.cookieBank.repository.entities.client.CompanyClientEntity;
import com.example.cookieBank.repository.entities.client.IndividualClientEntity;
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
    public ClientDTO saveClient(CreateClientDTO createClient, RoleClients role) {
        if (createClient.accountNumber().toLowerCase(Locale.ROOT).startsWith("system-"))
            throw new IllegalArgumentException("Account number can't starts with 'SYSTEM-'");

        return clientRepository.getDontAliveClientEntityByPhone(createClient.phone())
                .map(clientEntity -> {

                    if (clientEntity instanceof IndividualClientEntity individualClient) {
                        if (createClient.name() == null)
                            throw new IllegalArgumentException("Name can't must be null!");

                        if (!individualClient.getName().equals(createClient.name()))
                            throw new IllegalArgumentException("This phone number is already registered to another person!");
                    } else if (clientEntity instanceof CompanyClientEntity companyClient) {
                        if (createClient.companyName() == null)
                            throw new IllegalArgumentException("Company name can't must be null!");

                        if (!companyClient.getCompanyName().equals(createClient.companyName()))
                            throw new IllegalArgumentException("This phone number is already registered to another company!");
                    }

                    clientEntity.setAlive(true);
                    if (clientEntity.getAccount() != null)
                        clientEntity.getAccount().setAlive(true);

                    return converterToDtoService.convertClientToDTO(clientEntity);
                })
                .orElseGet(() -> {
                    String pin = passwordEncoder.encode(createClient.secretPin());
                    AccountEntity account = new AccountEntity(createClient.accountNumber(), pin);
                    ClientEntity newClient;

                    if (role == RoleClients.CLIENT) {
                        newClient = clientRepository.save(
                                new IndividualClientEntity(
                                        createClient.phone(),
                                        RoleClients.CLIENT,
                                        account,
                                        createClient.name(),
                                        createClient.lastName()
                                )
                        );
                    } else {
                        newClient = clientRepository.save(
                                new CompanyClientEntity(
                                        createClient.phone(),
                                        RoleClients.COMPANY,
                                        account,
                                        createClient.companyName()
                                )
                        );
                    }

                    return converterToDtoService.convertClientToDTO(newClient);
                });
    }

    @Transactional
    public ClientDTO updateClient(UpdateClientDTO updateClient) {
        ClientEntity client = clientRepository
                .findById(updateClient.id())
                .orElseThrow(() -> new NoResultException("client with id=" + updateClient.id() + " not found!"));

        if (client instanceof IndividualClientEntity individualClient) {
            if (updateClient.name() != null) {
                if (updateClient.name().equalsIgnoreCase("bank"))
                    throw new IllegalArgumentException("This name is reserved!");

                individualClient.setName(updateClient.name());
            }

            if (updateClient.lastName() != null) {
                individualClient.setLastName(updateClient.lastName());
            }
        } else if (client instanceof CompanyClientEntity companyClient) {
            if (updateClient.companyName() != null) {
                companyClient.setCompanyName(updateClient.companyName());
            }
        }

        if (updateClient.phone() != null) {
            client.setPhone(updateClient.phone());
        }

        return converterToDtoService.convertClientToDTO(client);
    }

    public ClientDTO getClientById(Long id) {
        ClientEntity client = clientRepository
                                .findById(id)
                                .orElseThrow(() -> new NoResultException("client with id=" + id + " not found!"));

        return converterToDtoService.convertClientToDTO(client);
    }

    public List<ClientDTO> getAllClient() {
        return clientRepository
                .findAll()
                .stream()
                .map(converterToDtoService::convertClientToDTO)
                .toList();
    }

    @Transactional
    public void deleteClientById(Long id) {
        ClientEntity client = clientRepository
                                .findById(id)
                                .orElseThrow(() -> new NoResultException("client with id=" + id + " not found!"));

        if (!client.getAlive())
            throw new IllegalArgumentException("the client was deleted earlier");

        client.setAlive(false);
        client.getAccount().setAlive(false);
    }

    public PaymentDTO createPayment(RequestPaymentDTO requestPayment, Long id) {
        ClientEntity client = clientRepository
                                .findById(id)
                                .orElseThrow(() -> new NoResultException("client with id=" + id + " not found!"));

        if (client.getAccount() == null)
            throw new IllegalStateException("Account not fount on client with id=" + id);

        return paymentService.clientPayment(requestPayment, client.getAccount().getNumber());
    }
}
