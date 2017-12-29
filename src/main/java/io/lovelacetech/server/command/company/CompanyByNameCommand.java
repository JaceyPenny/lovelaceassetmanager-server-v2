package io.lovelacetech.server.command.company;

import com.google.common.base.Strings;
import io.lovelacetech.server.command.Responds;
import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.CompanyApiResponse;

public class CompanyByNameCommand extends CompanyCommand<CompanyByNameCommand> implements Responds<CompanyApiResponse> {
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

    return new CompanyApiResponse()
        .setStatus(apiCompany != null ? Status.SUCCESS : Status.NOT_FOUND)
        .setMessage(apiCompany != null ? "success" : "no company found for name " + name)
        .setCompany(apiCompany);
  }
}
