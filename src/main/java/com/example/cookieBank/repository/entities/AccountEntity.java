package com.example.cookieBank.repository.entities;

import com.example.cookieBank.repository.entities.client.ClientEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "account_id",
            nullable = false
    )
    private Long id;

    @Column(
            name = "number",
            nullable = false,
            unique = true
    )
    private String number;

    @Column(
            name = "account_access_pin",
            nullable = false
    )
    private String accountAccessPin;

    @Column(
            name = "balance",
            nullable = false
    )
    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    private ClientEntity client;

    @Column(
            name = "is_alive",
            nullable = false
    )
    private Boolean isAlive;

    @OneToMany(mappedBy = "withAccount")
    private Set<TransactionalEntity> transactions;

    public AccountEntity() {}

    public AccountEntity(String number, String accountAccessPin) {
        this.number = number;
        this.accountAccessPin = accountAccessPin;
        this.balance = new BigDecimal(0);
        this.isAlive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccountAccessPin() {
        return accountAccessPin;
    }

    public void setAccountAccessPin(String accountAccessPin) {
        this.accountAccessPin = accountAccessPin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public Set<TransactionalEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionalEntity> transactions) {
        this.transactions = transactions;
    }
}
