package io.lovelacetech.lovelaceassetmanager.commands.companies;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.api.Company;
import io.lovelacetech.lovelaceassetmanager.models.api.enums.CompanyApiRequestStatus;
import io.lovelacetech.lovelaceassetmanager.models.entities.CompanyEntity;
import io.lovelacetech.lovelaceassetmanager.models.repositories.CompanyRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class CompanySaveCommand implements ResultCommandInterface<Company> {

    private CompanyRepository companyRepository;

    private Company apiCompany;

    public CompanySaveCommand() {
        this.companyRepository = new CompanyRepository();
    }

    @Override
    public Company execute() {
        if (StringUtils.isBlank(this.apiCompany.getName())
                || StringUtils.isBlank(this.apiCompany.getEmail())
                || StringUtils.isBlank(this.apiCompany.getPhoneNumber())
                || StringUtils.isBlank(this.apiCompany.getPassword())) {
            return (new Company()).setApiRequestStatus(CompanyApiRequestStatus.INVALID_INPUT);
        }

        CompanyEntity companyEntity = this.companyRepository.get(this.apiCompany.getId());
        if (companyEntity != null) {
            this.apiCompany = companyEntity.synchronize(apiCompany);
        } else {
            companyEntity = this.companyRepository.byCredentials(this.apiCompany.getEmail(), this.apiCompany.getPassword());
            if (companyEntity == null) {
                companyEntity = new CompanyEntity(this.apiCompany);
            } else {
                return (new Company()).setApiRequestStatus(CompanyApiRequestStatus.COMPANY_ALREADY_EXISTS);
            }
        }

        companyEntity.save();
        if ((new UUID(0, 0)).equals(this.apiCompany.getId())) {
            this.apiCompany.setId(companyEntity.getId());
        }

        return this.apiCompany;
    }

    public Company getApiCompany() {
        return apiCompany;
    }

    public CompanySaveCommand setApiCompany(Company apiCompany) {
        this.apiCompany = apiCompany;
        return this;
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public CompanySaveCommand setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        return this;
    }
}
