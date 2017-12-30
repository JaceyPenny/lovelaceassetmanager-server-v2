package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.company.CompanyByNameCommand;
import io.lovelacetech.server.command.company.CompanyByNameOrPhoneNumberCommand;
import io.lovelacetech.server.command.company.CompanyByPhoneNumberCommand;
import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.model.api.response.company.CompanyListApiResponse;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

  @Autowired
  CompanyRepository companyRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanies() {
    return new CompanyListApiResponse()
        .setStatus(Status.SUCCESS)
        .setMessage(Messages.SUCCESS)
        .setResponse(companyRepository.findAll());
  }

  @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyByName(@PathVariable String name) {
    return new CompanyByNameCommand()
        .setCompanyRepository(companyRepository)
        .setName(name)
        .execute();
  }

  @RequestMapping(value = "/byNameOrPhoneNumber/{name}/{phoneNumber}", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanyByNameOrPhoneNumber(
      @PathVariable String name,
      @PathVariable String phoneNumber) {
    return new CompanyByNameOrPhoneNumberCommand()
        .setCompanyRepository(companyRepository)
        .setName(name)
        .setPhoneNumber(phoneNumber)
        .execute();
  }

  @RequestMapping(value = "/byPhoneNumber/{phoneNumber}", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyByPhoneNumber(@PathVariable String phoneNumber) {
    return new CompanyByPhoneNumberCommand()
        .setCompanyRepository(companyRepository)
        .setPhoneNumber(phoneNumber)
        .execute();
  }
}
