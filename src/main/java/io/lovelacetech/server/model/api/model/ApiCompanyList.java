package io.lovelacetech.server.model.api.model;

import com.google.common.collect.Streams;
import io.lovelacetech.server.model.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiCompanyList extends BaseApiModel {
  List<ApiCompany> companies;

  public ApiCompanyList() {
    this.companies = new ArrayList<>();
  }

  public ApiCompanyList(Iterable<Company> companies) {
    this.companies = Streams.stream(companies)
        .map(Company::toApi)
        .collect(Collectors.toList());
  }

  public void setCompanies(List<ApiCompany> companies) {
    this.companies = companies;
  }

  public ApiCompanyList addCompany(ApiCompany company) {
    companies.add(company);
    return this;
  }

  public List<ApiCompany> getCompanies() {
    return companies;
  }
}
