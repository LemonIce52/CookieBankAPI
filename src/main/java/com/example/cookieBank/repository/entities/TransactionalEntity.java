package com.example.cookieBank.repository.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "transactional_id",
            nullable = false
    )
    private Long id;

    @Column(
            name = "date_time",
            nullable = false
    )
    private LocalDateTime dateTime;

    @Column(
            name = "sum_transaction",
            nullable = false
    )
    private BigDecimal sumTransaction;

    @ManyToOne
    @JoinColumn(
            name = "with_account",
            referencedColumnName = "account_id"
    )
    private AccountEntity withAccount;

    @ManyToOne
    @JoinColumn(
            name = "to_account",
            referencedColumnName = "account_id"
    )
    private AccountEntity toAccount;

    public TransactionalEntity() {}

    public TransactionalEntity(
            LocalDateTime dateTime,
            BigDecimal sumTransaction,
            AccountEntity withAccount,
            AccountEntity toAccount
    ) {
        this.dateTime = dateTime;
        this.sumTransaction = sumTransaction;
        this.withAccount = withAccount;
        this.toAccount = toAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getSumTransaction() {
        return sumTransaction;
    }

    public void setSumTransaction(BigDecimal sumTransaction) {
        this.sumTransaction = sumTransaction;
    }

    public AccountEntity getWithAccount() {
        return withAccount;
    }

    public void setWithAccount(AccountEntity withAccount) {
        this.withAccount = withAccount;
    }

    public AccountEntity getToAccount() {
        return toAccount;
    }

    public void setToAccount(AccountEntity toAccount) {
        this.toAccount = toAccount;
    }
}
