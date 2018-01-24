package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.company.*;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.model.api.response.company.CompanyListApiResponse;
import io.lovelacetech.server.repository.*;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/secure/companies")
public class CompanyController extends BaseController {

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  LocationRepository locationRepository;

  @Autowired
  AssetRepository assetRepository;

  @Autowired
  DeviceRepository deviceRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanies(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new CompanyListApiResponse()
        .setStatus(HttpStatus.OK)
        .setMessage(Messages.SUCCESS)
        .setResponse(companyRepository.findAll());
  }

  @RequestMapping(value = "/byCompanyId/{companyId}")
  public CompanyApiResponse getCompanyByCompanyId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID companyId,
      @RequestParam(defaultValue = "false") boolean filled) {
    return new CompanyByCompanyIdCommand()
        .setCompanyRepository(companyRepository)
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .setUser(authenticatedUser)
        .setCompanyId(companyId)
        .setFilled(filled)
        .execute();
  }

  @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyByName(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String name) {
    checkIsSuper(authenticatedUser);

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
    checkIsSuper(authenticatedUser);

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
    checkIsSuper(authenticatedUser);

    return new CompanyByPhoneNumberCommand()
        .setCompanyRepository(companyRepository)
        .setPhoneNumber(phoneNumber)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyForAuthenticatedUserFilled(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestParam(defaultValue = "true") boolean filled) {
    return new CompaniesForUserCommand()
        .setCompanyRepository(companyRepository)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .execute();
  }

  @RequestMapping(value="/forAuthenticated", method=RequestMethod.POST)
  public CompanyApiResponse putCompanyForAuthenticatedUser(
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
