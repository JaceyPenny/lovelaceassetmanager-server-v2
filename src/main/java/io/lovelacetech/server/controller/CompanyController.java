package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.company.CompanyByNameCommand;
import io.lovelacetech.server.command.company.CompanyByNameOrPhoneNumberCommand;
import io.lovelacetech.server.command.company.CompanyByPhoneNumberCommand;
import io.lovelacetech.server.command.company.SaveCompanyCommand;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.model.api.response.company.CompanyListApiResponse;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/companies")
public class CompanyController {

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  UserRepository userRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanies(@RequestAttribute ApiUser authenticatedUser) {
    if (!AuthenticationUtils.userIsSuper(authenticatedUser)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }

    return new CompanyListApiResponse()
        .setStatus(HttpStatus.OK)
        .setMessage(Messages.SUCCESS)
        .setResponse(companyRepository.findAll());
  }

  @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyByName(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String name) {
    if (!AuthenticationUtils.userIsSuper(authenticatedUser)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }

    return new CompanyByNameCommand()
        .setCompanyRepository(companyRepository)
        .setName(name)
        .execute();
  }

  @RequestMapping(value = "/byNameOrPhoneNumber/{name}/{phoneNumber}", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanyByNameOrPhoneNumber(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String name,
      @PathVariable String phoneNumber) {
    if (!AuthenticationUtils.userIsSuper(authenticatedUser)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }

    return new CompanyByNameOrPhoneNumberCommand()
        .setCompanyRepository(companyRepository)
        .setName(name)
        .setPhoneNumber(phoneNumber)
        .execute();
  }

  @RequestMapping(value = "/byPhoneNumber/{phoneNumber}", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyByPhoneNumber(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String phoneNumber) {
    if (!AuthenticationUtils.userIsSuper(authenticatedUser)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }

    return new CompanyByPhoneNumberCommand()
        .setCompanyRepository(companyRepository)
        .setPhoneNumber(phoneNumber)
        .execute();
  }

  @RequestMapping(value="/", method=RequestMethod.PUT)
  public CompanyApiResponse putCompany(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiCompany company) {
    return new SaveCompanyCommand()
        .setCompanyRepository(companyRepository)
        .setCompany(company)
        .setUserRepository(userRepository)
        .setUser(authenticatedUser)
        .execute();
  }
}
