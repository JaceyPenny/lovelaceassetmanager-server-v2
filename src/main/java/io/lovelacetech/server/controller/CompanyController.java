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
@CrossOrigin
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

  /**
   * <b>  GET /api/secure/companies/  </b>
   * <br><br>
   * Gets all the Companies in the database and returns in a list.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "companies": [Company]
   *   }
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * User must be SUPER.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public CompanyListApiResponse getCompanies(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new CompanyListApiResponse()
        .setStatus(HttpStatus.OK)
        .setMessage(Messages.SUCCESS)
        .setResponse(companyRepository.findAll());
  }

  /**
   * <b>  GET /api/secure/companies/byCompanyId/{companyId}  </b>
   * <br><br>
   * Gets the Company with id "companyId"
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Company
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * User must either be SUPER, or must be a member of the Company with id "companyId"; That is to
   * say, the user's companyId must match the "companyId" from the query.
   *
   */
  @RequestMapping(value = "/byCompanyId/{companyId}", method = RequestMethod.GET)
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

  /**
   * <b>  GET /api/secure/companies/byName/{name}  </b>
   * <br><br>
   * Gets the Company with name "name"
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Company
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * User must be SUPER.
   */
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

  /**
   * <b>  GET /api/secure/companies/byNameOrPhoneNumber/{name}/{phoneNumber}  </b>
   * <br><br>
   * Gets any Companies whose names or phone numbers match those from the query.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "companies": [Company]
   *   }
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * User must be SUPER.
   */
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

  /**
   * <b>  GET /api/secure/companies/byPhoneNumber/{phoneNumber}  </b>
   * <br><br>
   * Gets the Company with phoneNumber "phoneNumber"
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Company
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * User must be SUPER.
   */
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

  /**
   * <b>  GET /api/secure/companies/forAuthenticated?filled={true|false}  </b>
   * <br><br>
   * Gets the Company that the authenticatedUser is associated with. That is to say, gets the
   * company whose id matches the user's "companyId". The "filled" parameters specifies whether
   * or not the caller wants the Company's "locations" list populated. The population process is
   * recursive. See {@link io.lovelacetech.server.util.LoaderUtils}.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Company
   * }
   * }
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public CompanyApiResponse getCompanyForAuthenticatedUserFilled(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestParam(defaultValue = "true") boolean filled) {
    checkBelongsToCompany(authenticatedUser);

    return new CompaniesForUserCommand()
        .setCompanyRepository(companyRepository)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .execute();
  }

  /**
   * <b>  POST /api/secure/companies/forAuthenticated  </b>
   * <br><br>
   * This endpoint is to CREATE or UPDATE Companies.
   * <br><br>
   * To <b>CREATE</b> a Company, supply the body in the following manner:
   * <br>
   * {@code
   * {
   *   (required) "name": ...,
   *   (required) "phoneNumber": ...
   * }
   * }
   * <br>
   * Once the Company is created, the calling user has their "companyId" set to the newly created
   * Company's id, and their "accessLevel" set to ADMIN.
   * <br><br>
   * To <b>UPDATE</b> a Company, supply the body in the following manner:
   * {@code
   * {
   *   (required) "id": ...,
   *   (optional) "name": ...,
   *   (optional) "phoneNumber": ...
   * }
   * }
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Company
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * If the calling user is updating a Company, they must be the ADMIN for that Company. Any user
   * is allowed to create a Company.
   */
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
