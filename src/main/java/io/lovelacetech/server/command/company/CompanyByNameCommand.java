package io.lovelacetech.server.command.company;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;

public class CompanyByNameCommand extends CompanyCommand<CompanyByNameCommand> {
  private String name;

  public CompanyByNameCommand setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && !Strings.isNullOrEmpty(this.name);
  }

  @Override
  public CompanyApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyApiResponse().setDefault();
    }

    ApiCompany apiCompany = null;
    try {
      apiCompany = getCompanyRepository().findByName(name).toApi();
    } catch (NullPointerException ignored) {
    }

    if (apiCompany == null) {
      return new CompanyApiResponse().setNotFoundByName(name);
    } else {
      return new CompanyApiResponse()
          .setSuccess()
          .setResponse(apiCompany);
    }
  }
}
