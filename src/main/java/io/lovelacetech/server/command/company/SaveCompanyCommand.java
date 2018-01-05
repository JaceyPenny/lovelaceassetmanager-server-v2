package io.lovelacetech.server.command.company;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class SaveCompanyCommand extends CompanyCommand<SaveCompanyCommand> {
  private ApiCompany company;

  public SaveCompanyCommand setCompany(ApiCompany company) {
    this.company = company;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && this.company != null;
  }

  @Override
  public CompanyApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyApiResponse().setDefault();
    }

    Company companyUpdate = company.toDatabase();
    if (companyUpdate.hasId()) {
      Company existingCompany = getCompanyRepository().findOne(companyUpdate.getId());

      if (existingCompany == null) {
        return new CompanyApiResponse().setNotFound();
      } else {
        System.out.println(existingCompany.toApi());
        existingCompany.applyUpdate(companyUpdate);
        System.out.println(existingCompany.toApi());
        companyUpdate = existingCompany;
      }
    }

    Company existingCompanyWithName = getCompanyRepository().findByName(company.getName());
    if (existingCompanyWithName != null) {
      if (!companyUpdate.hasId()
          || (companyUpdate.hasId() && !companyUpdate.idEquals(existingCompanyWithName.getId()))) {
        return new CompanyApiResponse()
            .setStatus(HttpStatus.CONFLICT)
            .setMessage(Messages.COMPANY_CONFLICTING_NAME);
      }
    }

    getCompanyRepository().save(companyUpdate);

    return new CompanyApiResponse()
        .setSuccess()
        .setResponse(companyUpdate.toApi());
  }
}
