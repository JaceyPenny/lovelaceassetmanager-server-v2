package io.lovelacetech.server.model.api.response.company;

import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class CompanyApiResponse extends BaseApiResponse<CompanyApiResponse, ApiCompany> {
  public CompanyApiResponse setNotFoundByName(String name) {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_COMPANY_FOUND_BY_NAME(name));
    setResponse(null);
    return this;
  }

  public CompanyApiResponse setNotFoundByPhoneNumber(String phoneNumber) {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_COMPANY_FOUND_BY_PHONE_NUMBER(phoneNumber));
    setResponse(null);
    return this;
  }
}
