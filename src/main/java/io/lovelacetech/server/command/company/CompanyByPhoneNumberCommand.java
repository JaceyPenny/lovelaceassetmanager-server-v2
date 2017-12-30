package io.lovelacetech.server.command.company;

import io.lovelacetech.server.command.Responds;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import org.h2.util.StringUtils;

public class CompanyByPhoneNumberCommand extends CompanyCommand<CompanyByPhoneNumberCommand> implements Responds<CompanyApiResponse> {

  private String phoneNumber;

  public CompanyByPhoneNumberCommand setPhoneNumber(String name) {
    this.phoneNumber = name;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && !StringUtils.isNullOrEmpty(phoneNumber);
  }

  @Override
  public CompanyApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyApiResponse().setDefault();
    }

    ApiCompany apiCompany = null;
    try {
      apiCompany = getCompanyRepository().findByPhoneNumber(phoneNumber).toApi();
    } catch (NullPointerException ignored) {}

    if (apiCompany == null) {
      return new CompanyApiResponse().setNotFoundByPhoneNumber(phoneNumber);
    } else {
      return new CompanyApiResponse()
          .setSuccess()
          .setResponse(apiCompany);
    }
  }
}
