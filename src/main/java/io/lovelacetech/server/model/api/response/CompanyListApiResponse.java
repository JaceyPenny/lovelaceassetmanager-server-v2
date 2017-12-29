package io.lovelacetech.server.model.api.response;

import io.lovelacetech.server.model.api.model.ApiCompanyList;

public class CompanyListApiResponse extends BaseApiResponse<CompanyListApiResponse, ApiCompanyList> {
  private ApiCompanyList companyList;

  @Override
  public CompanyListApiResponse setDefault() {
    super.setDefault();
    companyList = null;
    return this;
  }

  public CompanyListApiResponse setCompanyList(ApiCompanyList companyList) {
    this.companyList = companyList;
    return this;
  }

  @Override
  public ApiCompanyList getResponse() {
    return companyList;
  }
}
