package com.example.cookieBank.repository.entities.client;

import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.RoleClients;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INDIVIDUAL")
public class IndividualClientEntity extends ClientEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    public IndividualClientEntity() {}

    public IndividualClientEntity(String phone, RoleClients role, AccountEntity account, String name, String lastName) {
        super(phone, role, account);
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
