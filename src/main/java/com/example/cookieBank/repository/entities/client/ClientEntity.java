package com.example.cookieBank.repository.entities.client;

import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.RoleClients;
import com.example.cookieBank.repository.entities.TokensEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator_type")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "client_id",
            nullable = false
    )
    private Long id;

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

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false
    )
    private RoleClients role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "account",
            referencedColumnName = "account_id"
    )
    private AccountEntity account;

    @OneToOne(mappedBy = "client")
    private TokensEntity token;

    public ClientEntity() {}

    public ClientEntity(String phone, RoleClients role, AccountEntity account) {
        this.phone = phone;
        this.account = account;
        this.role = role;
        this.isAlive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RoleClients getRole() {
        return role;
    }

    public void setRole(RoleClients role) {
        this.role = role;
    }

    public TokensEntity getToken() {
        return token;
    }

    public void setToken(TokensEntity token) {
        this.token = token;
    }
}
