package com.example.cookieBank.repository.impl;

import com.example.cookieBank.repository.entities.TransactionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionalRepository extends JpaRepository<TransactionalEntity, Long> {
}
