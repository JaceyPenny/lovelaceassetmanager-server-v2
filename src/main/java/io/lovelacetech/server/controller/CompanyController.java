package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.company.CompanyByNameCommand;
import io.lovelacetech.server.command.company.CompanyByNameOrPhoneNumberCommand;
import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiCompanyList;
import io.lovelacetech.server.model.api.response.CompanyApiResponse;
import io.lovelacetech.server.model.api.response.CompanyListApiResponse;
import io.lovelacetech.server.repository.CompanyRepository;
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
    Iterable<Company> companies = companyRepository.findAll();
    CompanyListApiResponse apiResponse = new CompanyListApiResponse();
    apiResponse.setStatus(Status.SUCCESS);
    apiResponse.setMessage("Success.");
    ApiCompanyList companyList = new ApiCompanyList();
    companies.forEach(company -> companyList.addCompany(new ApiCompany(company)));
    apiResponse.setCompanyList(companyList);
    return apiResponse;
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
}
