package io.lovelacetech.lovelaceassetmanager.commands.companies;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.api.Company;
import io.lovelacetech.lovelaceassetmanager.models.api.enums.CompanyApiRequestStatus;
import io.lovelacetech.lovelaceassetmanager.models.entities.CompanyEntity;
import io.lovelacetech.lovelaceassetmanager.models.repositories.CompanyRepository;
import org.apache.commons.lang3.StringUtils;

public class CompanyByCredentialsQuery implements ResultCommandInterface<Company> {

    private CompanyRepository companyRepository;

    private String email;
    private String password;

    public CompanyByCredentialsQuery() {
        this.companyRepository = new CompanyRepository();
    }

    @Override
    public Company execute() {
        if (StringUtils.isBlank(this.email) || StringUtils.isBlank(this.password)) {
            return new Company().setApiRequestStatus(CompanyApiRequestStatus.INVALID_INPUT);
        }

        CompanyEntity companyEntity = this.companyRepository.byCredentials(this.email, this.password);
        if (companyEntity != null) {
            return new Company(companyEntity);
        } else {
            return new Company().setApiRequestStatus(CompanyApiRequestStatus.NOT_FOUND);
        }
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public CompanyByCredentialsQuery setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        return this;
    }

    public String getEmail() {
        return email;
    }
    public CompanyByCredentialsQuery setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }
    public CompanyByCredentialsQuery setPassword(String password) {
        this.password = password;
        return this;
    }
}
