package io.lovelacetech.lovelaceassetmanager.commands.companies;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.api.Company;
import io.lovelacetech.lovelaceassetmanager.models.api.CompanyListing;
import io.lovelacetech.lovelaceassetmanager.models.repositories.CompanyRepository;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.CompanyRepositoryInterface;

import java.util.stream.Collectors;

public class CompaniesQuery implements ResultCommandInterface<CompanyListing> {
    private CompanyRepositoryInterface companyRepository;

    public CompaniesQuery() {
        this.companyRepository = new CompanyRepository();
    }

    @Override
    public CompanyListing execute() {
        return (new CompanyListing())
                .setCompanies(
                        this.companyRepository
                                .all()
                                .stream()
                                .map(company -> (new Company(company)))
                                .collect(Collectors.toList()));
    }

    public CompanyRepositoryInterface getCompanyRepository() {
        return companyRepository;
    }

    public CompaniesQuery setCompanyRepository(CompanyRepositoryInterface companyRepository) {
        this.companyRepository = companyRepository;
        return this;
    }
}
