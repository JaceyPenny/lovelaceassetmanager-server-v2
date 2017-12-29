package io.lovelacetech.server.model.api.response;

import io.lovelacetech.server.model.api.model.ApiCompany;

public class CompanyApiResponse extends BaseApiResponse<CompanyApiResponse, ApiCompany> {
  private ApiCompany company;

  @Override
  public CompanyApiResponse setDefault() {
    super.setDefault();
    company = null;
    return this;
  }

  public CompanyApiResponse setCompany(ApiCompany company) {
    this.company = company;
    return this;
  }

  @Override
  public ApiCompany getResponse() {
    return company;
  }
}
