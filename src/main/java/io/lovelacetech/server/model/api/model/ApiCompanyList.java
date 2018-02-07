package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiCompanyList extends BaseApiModel {
  private List<ApiCompany> companies;

  public ApiCompanyList() {
    this.companies = new ArrayList<>();
  }

  public ApiCompanyList(Iterable<Company> companies) {
    this.companies = RepositoryUtils.toApiList(companies);
  }

  public ApiCompanyList(List<ApiCompany> companies) {
    this.companies = companies;
  }

  public List<ApiCompany> getCompanies() {
    sort();
    return companies;
  }

  public ApiCompanyList setCompanies(List<ApiCompany> companies) {
    this.companies = companies;
    return this;
  }

  public ApiCompanyList addCompany(ApiCompany company) {
    companies.add(company);
    return this;
  }

  public void sort() {
    companies.forEach(ApiCompany::sort);
    companies.sort(Comparator.comparing(ApiCompany::getName));
  }
}
