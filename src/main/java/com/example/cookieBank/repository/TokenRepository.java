package com.example.cookieBank.repository;

import com.example.cookieBank.repository.entities.TokensEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokensEntity, Long> {

    @Query("select t from TokensEntity t left join fetch t.client c where c.id = :clientId")
    Optional<TokensEntity> getTokensEntitiesByClientId(Long clientId);

    @Modifying
    @Query("update TokensEntity t set t.refreshToken = :refreshToken where t.id = :id")
    void updateRefreshTokenById(String refreshToken, Long id);

}
