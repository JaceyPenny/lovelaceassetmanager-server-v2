package io.lovelacetech.server.model.api.response.company;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiCompanyList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

import java.util.List;

public class CompanyListApiResponse
    extends BaseApiResponse<CompanyListApiResponse, ApiCompanyList> {
  public CompanyListApiResponse setNotFound() {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_COMPANIES_FOUND);
    super.setResponse(null);
    return this;
  }

  @Override
  public ApiCompanyList getResponse() {
    super.getResponse().sort();
    return super.getResponse();
  }

  public CompanyListApiResponse setResponse(List<ApiCompany> companies) {
    setResponse(new ApiCompanyList(companies));
    return this;
  }

  public CompanyListApiResponse setResponse(Iterable<Company> companies) {
    setResponse(new ApiCompanyList(companies));
    return this;
  }
}
