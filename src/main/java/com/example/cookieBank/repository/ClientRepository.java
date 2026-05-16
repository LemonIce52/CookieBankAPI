package com.example.cookieBank.repository;

import com.example.cookieBank.repository.entities.client.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("select c from ClientEntity c left join fetch c.account where c.phone = :phone and c.isAlive = false and c.role not in ('ADMIN')")
    Optional<ClientEntity> getDontAliveClientEntityByPhone(
            String phone
    );

    @Query("select c from ClientEntity c left join fetch c.account where c.phone = :phone and c.role not in ('ADMIN')")
    Optional<ClientEntity> getClientEntityByPhone(
            String phone
    );


    @Query("select count(c) from ClientEntity c where c.role = 'ADMIN'")
    int getSystemClient();

}
