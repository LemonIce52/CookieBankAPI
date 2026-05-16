package com.example.cookieBank.component;

import com.example.cookieBank.repository.AccountRepository;
import com.example.cookieBank.repository.ClientRepository;
import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.client.ClientEntity;
import com.example.cookieBank.repository.entities.RoleClients;
import com.example.cookieBank.repository.entities.client.CompanyClientEntity;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ClientRepository clientRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (clientRepository.getSystemClient() < 1) {
            String pin = passwordEncoder.encode("B$A@N!K09_0213");
            AccountEntity account = new AccountEntity("SYSTEM-001", pin);
            account.setBalance(new BigDecimal("1000000000000"));
            accountRepository.save(account);

            ClientEntity client = new CompanyClientEntity("89990001515" , RoleClients.ADMIN, account, "ООО\"CookieBank\"");
            clientRepository.save(client);
        }
    }
}
