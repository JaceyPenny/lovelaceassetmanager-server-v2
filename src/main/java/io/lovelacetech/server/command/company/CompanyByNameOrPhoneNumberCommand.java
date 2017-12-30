package io.lovelacetech.server.command.company;

import com.google.common.base.Strings;
import io.lovelacetech.server.command.Responds;
import io.lovelacetech.server.model.api.model.ApiCompanyList;
import io.lovelacetech.server.model.api.response.company.CompanyListApiResponse;

public class CompanyByNameOrPhoneNumberCommand extends CompanyCommand<CompanyByNameOrPhoneNumberCommand> implements Responds<CompanyListApiResponse> {
  private String name;
  private String phoneNumber;

  public CompanyByNameOrPhoneNumberCommand setName(String name) {
    this.name = name;
    return this;
  }

  public CompanyByNameOrPhoneNumberCommand setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && !Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(phoneNumber);
  }

  @Override
  public CompanyListApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyListApiResponse().setDefault();
    }

    ApiCompanyList apiCompanyList = null;
    try {
      apiCompanyList = new ApiCompanyList(getCompanyRepository().findByNameOrPhoneNumber(name, phoneNumber));
    } catch (NullPointerException ignored) {
    }

    if (apiCompanyList == null) {
      return new CompanyListApiResponse().setNotFound();
    } else {
      return new CompanyListApiResponse()
          .setSuccess()
          .setResponse(apiCompanyList);
    }
  }
}
