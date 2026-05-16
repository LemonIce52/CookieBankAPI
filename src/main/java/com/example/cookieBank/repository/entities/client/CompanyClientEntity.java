package com.example.cookieBank.repository.entities.client;

import com.example.cookieBank.repository.entities.AccountEntity;
import com.example.cookieBank.repository.entities.RoleClients;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPANY")
public class CompanyClientEntity extends ClientEntity {

    @Column(name = "company_name")
    private String companyName;

    public CompanyClientEntity() {}

    public CompanyClientEntity(String phone, RoleClients role, AccountEntity account, String companyName) {
        super(phone, role, account);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
