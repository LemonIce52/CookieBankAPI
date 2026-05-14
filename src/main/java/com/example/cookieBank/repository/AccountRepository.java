package com.example.cookieBank.repository;

import com.example.cookieBank.repository.entities.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("select a from AccountEntity a left join fetch a.client c where c.id = :id and a.isAlive <> false and a.number not like 'SYSTEM-%'")
    Optional<AccountEntity> getAccountEntitiesByClientId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AccountEntity a where a.number = :number and a.isAlive <> false")
    Optional<AccountEntity> getAccountEntitiesByAccountNumberForUpdate(String number);

    @Query("select a from AccountEntity a left join fetch a.client where a.isAlive <> false and a.number not like 'SYSTEM-%'")
    List<AccountEntity> getAllAccounts();

}
