package com.example.cookieBank.repository;

import com.example.cookieBank.repository.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("select c from ClientEntity c left join fetch c.account where c.id = :id and c.isAlive <> false and c.name <> 'BANK'")
    Optional<ClientEntity> getClientEntityById(
            Long id
    );

    @Query("select c from ClientEntity c left join fetch c.account where c.phone = :phone and c.isAlive = false and c.name <> 'BANK'")
    Optional<ClientEntity> getDontAliveClientEntityByPhone(
            String phone
    );

    @Query("select c from ClientEntity c left join fetch c.account where c.phone = :phone and c.name <> 'BANK'")
    Optional<ClientEntity> getClientEntityByPhone(
            String phone
    );

    @Query("select count(c) from ClientEntity c where c.name = :name")
    int getSystemClient(String name);

    @Query("select c from ClientEntity c where c.isAlive <> false and c.name <> 'BANK'")
    List<ClientEntity> getAllClientEntity();
}
