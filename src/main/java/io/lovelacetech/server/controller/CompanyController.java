package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.company.CompanyByNameCommand;
import io.lovelacetech.server.command.company.CompanyByNameOrPhoneNumberCommand;
import io.lovelacetech.server.command.company.CompanyByPhoneNumberCommand;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.model.api.response.company.CompanyListApiResponse;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/companies")
public class CompanyController {

  @Autowired
  CompanyRepository companyRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanies() {
    return new CompanyListApiResponse()
        .setStatus(HttpStatus.OK)
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

  @RequestMapping(value="/", method=RequestMethod.PUT)
  public CompanyApiResponse putCompany(@RequestBody ApiCompany company) {
    System.out.println(company);

    return new CompanyApiResponse().setDefault();
  }
}
