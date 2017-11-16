package io.lovelacetech.lovelaceassetmanager.models.api;

import java.util.LinkedList;
import java.util.List;

public class CompanyListing {
    private List<Company> companies;

    public CompanyListing() {
        this.companies = new LinkedList<>();
    }

    public List<Company> getCompanies() {
        return this.companies;
    }
    public CompanyListing setCompanies(List<Company> companies) {
        this.companies = companies;
        return this;
    }
    public CompanyListing addCompany(Company company) {
        this.companies.add(company);
        return this;
    }
}
