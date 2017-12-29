package io.lovelacetech.server.command.company;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.repository.CompanyRepository;

public abstract class CompanyCommand<T extends CompanyCommand> implements BaseCommand {
  private CompanyRepository companyRepository;

  public T setCompanyRepository(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
    return (T) this;
  }

  CompanyRepository getCompanyRepository() {
    return companyRepository;
  }

  @Override
  public boolean checkCommand() {
    return companyRepository != null;
  }
}
