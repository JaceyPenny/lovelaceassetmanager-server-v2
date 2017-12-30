package io.lovelacetech.server.model.api.response.company;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.model.ApiCompanyList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;

public class CompanyListApiResponse extends BaseApiResponse<CompanyListApiResponse, ApiCompanyList> {
  private ApiCompanyList companyList;

  @Override
  public CompanyListApiResponse setDefault() {
    super.setDefault();
    companyList = null;
    return this;
  }

  public CompanyListApiResponse setNotFound() {
    setStatus(Status.NOT_FOUND);
    setMessage(Messages.NO_COMPANIES_FOUND);
    this.companyList = null;
    return this;
  }

  public CompanyListApiResponse setResponse(Iterable<Company> companies) {
    setResponse(new ApiCompanyList(companies));
    return this;
  }

  @Override
  public CompanyListApiResponse setResponse(ApiCompanyList companyList) {
    this.companyList = companyList;
    return this;
  }

  @Override
  public ApiCompanyList getResponse() {
    return companyList;
  }
}
