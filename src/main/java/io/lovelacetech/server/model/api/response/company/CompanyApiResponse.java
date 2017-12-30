package io.lovelacetech.server.model.api.response.company;

import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;

public class CompanyApiResponse extends BaseApiResponse<CompanyApiResponse, ApiCompany> {
  private ApiCompany company;

  @Override
  public CompanyApiResponse setDefault() {
    super.setDefault();
    company = null;
    return this;
  }

  public CompanyApiResponse setNotFoundByName(String name) {
    setStatus(Status.NOT_FOUND);
    setMessage(Messages.NO_COMPANY_FOUND_BY_NAME(name));
    setResponse(null);
    return this;
  }

  public CompanyApiResponse setNotFoundByPhoneNumber(String phoneNumber) {
    setStatus(Status.NOT_FOUND);
    setMessage(Messages.NO_COMPANY_FOUND_BY_PHONE_NUMBER(phoneNumber));
    setResponse(null);
    return this;
  }

  public CompanyApiResponse setResponse(ApiCompany company) {
    this.company = company;
    return this;
  }

  @Override
  public ApiCompany getResponse() {
    return company;
  }
}
