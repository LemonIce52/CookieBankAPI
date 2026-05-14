package com.example.cookieBank.repository.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class TokensEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "client_id",
            referencedColumnName = "client_id"
    )
    private ClientEntity client;

    @Column(
            name = "refresh_token",
            nullable = false
    )
    private String refreshToken;

    public TokensEntity() {}

    public TokensEntity(ClientEntity client, String refreshToken) {
        this.client = client;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
