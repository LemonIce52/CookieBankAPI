package com.example.cookieBank.repository;

import com.example.cookieBank.repository.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("select a from AccountEntity a left join fetch a.client where a.id = :id and a.isAlive <> false")
    AccountEntity getAccountEntitiesById(Long id);

    @Query("select a from AccountEntity a left join fetch a.client where a.isAlive <> false")
    List<AccountEntity> getAllAccounts();

}
