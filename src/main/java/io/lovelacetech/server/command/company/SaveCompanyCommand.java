package io.lovelacetech.server.command.company;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;

public class SaveCompanyCommand extends CompanyCommand<SaveCompanyCommand> {
  private ApiCompany company;
  private ApiUser user;
  private UserRepository userRepository;

  public SaveCompanyCommand setCompany(ApiCompany company) {
    this.company = company;
    return this;
  }

  public SaveCompanyCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SaveCompanyCommand setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && this.company != null
        && ((user == null && userRepository == null)
            || (user != null && userRepository != null));
  }

  @Override
  public CompanyApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyApiResponse().setDefault();
    }

    // Remembers if this is a new company being created
    boolean isNewCompany = false;

    // If the user supplied "id" with their request, fetch the existing Company
    // for that ID. Otherwise, throw "Not Found"
    Company companyUpdate = company.toDatabase();
    if (companyUpdate.hasId()) {
      Company existingCompany = getCompanyRepository().findOne(companyUpdate.getId());

      if (existingCompany == null) {
        return new CompanyApiResponse().setNotFound();
      } else {
        existingCompany.applyUpdate(companyUpdate);
        companyUpdate = existingCompany;
      }
    } else {
      isNewCompany = true;
    }

    // A Company cannot be added to the database if its name is taken by another company
    Company existingCompanyWithName = getCompanyRepository().findByName(company.getName());
    if (existingCompanyWithName != null
        && RepositoryUtils.updateConflictsWithExistingRow(companyUpdate, existingCompanyWithName)) {
      return new CompanyApiResponse()
          .setStatus(HttpStatus.CONFLICT)
          .setMessage(Messages.COMPANY_CONFLICTING_NAME);
    }

    companyUpdate = getCompanyRepository().save(companyUpdate);

    if (isNewCompany && user != null) {
      user.setCompanyId(companyUpdate.getId());
      user.setAccessLevel(AccessLevel.ADMIN);

      User result = userRepository.save(user.toDatabase());
      System.out.println(result.toApi());
    }

    return new CompanyApiResponse()
        .setSuccess()
        .setResponse(companyUpdate.toApi());
  }
}
