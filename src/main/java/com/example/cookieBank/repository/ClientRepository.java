package com.example.cookieBank.repository;

import com.example.cookieBank.repository.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("select c from ClientEntity c left join fetch c.account where c.id = :id and c.isAlive <> false")
    ClientEntity getClientEntityById(
            Long id
    );

    @Query("select c from ClientEntity c left join fetch c.account where c.phone = :phone and c.name = :name and c.isAlive = false")
    ClientEntity getClientEntityByPhoneAndName(
            String name,
            String phone
    );

    @Query("select c from ClientEntity c where c.isAlive <> false")
    List<ClientEntity> getAllClientEntity();
}
