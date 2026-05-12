package com.example.cookieBank.repository.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "client_id",
            nullable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;

    @Column(
            name = "phone",
            nullable = false,
            unique = true
    )
    private String phone;

    @Column(
            name = "is_alive",
            nullable = false
    )
    private Boolean isAlive;

    @OneToOne
    @JoinColumn(
            name = "account",
            referencedColumnName = "account_id"
    )
    private AccountEntity account;

    public ClientEntity() {}

    public ClientEntity(String name, String lastName, String phone, AccountEntity account) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.account = account;
        this.isAlive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }
}
