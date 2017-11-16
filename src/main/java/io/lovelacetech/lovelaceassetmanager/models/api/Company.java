package io.lovelacetech.lovelaceassetmanager.models.api;

import io.lovelacetech.lovelaceassetmanager.models.api.enums.CompanyApiRequestStatus;
import io.lovelacetech.lovelaceassetmanager.models.entities.CompanyEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class Company {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    private String apiRequestMessage;
    private CompanyApiRequestStatus apiRequestStatus;
    // TODO: Implement Locations and Users
//    private List<Location> locations;
//    private List<User> users;

    public Company() {
        this.id = new UUID(0, 0);
        this.name = StringUtils.EMPTY;
        this.email = StringUtils.EMPTY;
        this.phoneNumber = StringUtils.EMPTY;
        this.password = StringUtils.EMPTY;
    }

    public Company(CompanyEntity companyEntity) {
        this.id = companyEntity.getId();
        this.name = companyEntity.getName();
        this.email = companyEntity.getEmail();
        this.phoneNumber = companyEntity.getPhoneNumber();
        this.password = companyEntity.getPassword();

        this.apiRequestMessage = StringUtils.EMPTY;
        this.apiRequestStatus = CompanyApiRequestStatus.OK;
    }

    public UUID getId() {
        return this.id;
    }
    public Company setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }
    public Company setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return this.email;
    }
    public Company setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Company setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getPassword() {
        return password;
    }
    public Company setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getApiRequestMessage() {
        return apiRequestMessage;
    }
    public Company setApiRequestMessage(String apiRequestMessage) {
        if (!StringUtils.equalsIgnoreCase(this.apiRequestMessage, apiRequestMessage)) {
            this.apiRequestMessage = apiRequestMessage;
        }
        return this;
    }

    public CompanyApiRequestStatus getApiRequestStatus() {
        return apiRequestStatus;
    }

    public Company setApiRequestStatus(CompanyApiRequestStatus apiRequestStatus) {
        if (this.apiRequestStatus != apiRequestStatus) {
            this.apiRequestStatus = apiRequestStatus;
        }
        return this;
    }
}
