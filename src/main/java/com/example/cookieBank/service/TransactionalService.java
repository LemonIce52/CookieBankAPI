package com.example.cookieBank.service;

import com.example.cookieBank.dto.transactional.CreateTransactionalDTO;
import com.example.cookieBank.dto.transactional.TransactionalDTO;
import com.example.cookieBank.repository.TransactionalRepository;
import com.example.cookieBank.repository.entities.TransactionalEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionalService {

    private final TransactionalRepository transactionalRepository;
    private final ConverterToDtoService converterToDtoService;

    public TransactionalService(TransactionalRepository transactionalRepository, ConverterToDtoService converterToDtoService) {
        this.transactionalRepository = transactionalRepository;
        this.converterToDtoService = converterToDtoService;
    }

    @Transactional
    public TransactionalDTO createTransactional(CreateTransactionalDTO createTransactional){
        TransactionalEntity savedTransactional = transactionalRepository.save(
                new TransactionalEntity(
                        LocalDateTime.now(),
                        createTransactional.sum(),
                        createTransactional.withAccount(),
                        createTransactional.toAccount()
                )
        );

        return converterToDtoService.convertTransactionalToDTO(savedTransactional);
    }

}
