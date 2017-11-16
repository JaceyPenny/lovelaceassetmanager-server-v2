package io.lovelacetech.lovelaceassetmanager.models.entities;

import io.lovelacetech.lovelaceassetmanager.dataaccess.entities.BaseEntity;
import io.lovelacetech.lovelaceassetmanager.models.api.Company;
import io.lovelacetech.lovelaceassetmanager.models.entities.fieldnames.CompanyFieldNames;
import io.lovelacetech.lovelaceassetmanager.models.repositories.CompanyRepository;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class CompanyEntity extends BaseEntity<CompanyEntity> {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    public CompanyEntity() {
        super(new CompanyRepository());

        this.name = StringUtils.EMPTY;
        this.email = StringUtils.EMPTY;
        this.phoneNumber = StringUtils.EMPTY;
        this.password = StringUtils.EMPTY;
    }

    public CompanyEntity(UUID id) {
        super(id, new CompanyRepository());

        this.name = StringUtils.EMPTY;
        this.email = StringUtils.EMPTY;
        this.phoneNumber = StringUtils.EMPTY;
        this.password = StringUtils.EMPTY;
    }

    public CompanyEntity(Company apiCompany) {
        super(apiCompany.getId(), new CompanyRepository());

        this.name = apiCompany.getName();
        this.email = apiCompany.getEmail();
        this.phoneNumber = apiCompany.getPhoneNumber();
        this.password = apiCompany.getPassword();
    }

    @Override
    protected void fillFromRecord(ResultSet rs) throws SQLException {
        this.name = rs.getString(CompanyFieldNames.NAME);
        this.email = rs.getString(CompanyFieldNames.EMAIL);
        this.phoneNumber = rs.getString(CompanyFieldNames.PHONE_NUMBER);
        this.password = rs.getString(CompanyFieldNames.PASSWORD);
    }

    @Override
    protected Map<String, Object> fillRecord(Map<String, Object> record) {
        record.put(CompanyFieldNames.NAME, this.name);
        record.put(CompanyFieldNames.EMAIL, this.email);
        record.put(CompanyFieldNames.PHONE_NUMBER, this.phoneNumber);
        record.put(CompanyFieldNames.PASSWORD, this.password);
        return record;
    }

    public Company synchronize(Company apiCompany) {
        this.setName(apiCompany.getName());
        this.setEmail(apiCompany.getEmail());
        this.setPhoneNumber(apiCompany.getPhoneNumber());
        this.setPassword(apiCompany.getPassword());

        return apiCompany;
    }

    public String getName() {
        return name;
    }
    public CompanyEntity setName(String name) {
        if (!StringUtils.equals(this.name, name)) {
            this.name = name;
            this.propertyChanged(CompanyFieldNames.NAME);
        }
        return this;
    }

    public String getEmail() {
        return email;
    }
    public CompanyEntity setEmail(String email) {
        if (!StringUtils.equals(this.email, email)) {
            this.email = email;
            this.propertyChanged(CompanyFieldNames.EMAIL);
        }
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public CompanyEntity setPhoneNumber(String phoneNumber) {
        if (!StringUtils.equals(this.phoneNumber, phoneNumber)) {
            this.phoneNumber = phoneNumber;
            this.propertyChanged(CompanyFieldNames.PHONE_NUMBER);
        }
        return this;
    }

    public String getPassword() {
        return password;
    }
    public CompanyEntity setPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            this.password = password;
            this.propertyChanged(CompanyFieldNames.PASSWORD);
        }
        return this;
    }
}
