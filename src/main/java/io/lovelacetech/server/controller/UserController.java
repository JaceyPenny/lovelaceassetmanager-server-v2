package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.user.*;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiLocationIdList;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/secure/users")
public class UserController extends BaseController {

  private final UserRepository userRepository;
  private final LocationRepository locationRepository;

  @Autowired
  public UserController(UserRepository userRepository, LocationRepository locationRepository) {
    this.userRepository = userRepository;
    this.locationRepository = locationRepository;
  }

  /**
   * <b>  GET /api/secure/users/  </b>
   * <br><br>
   * Gets all the Users in the database and returns in a list.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "users": [User]
   *   }
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be SUPER to access this endpoint.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public UserListApiResponse getUsers(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new UserListApiResponse()
        .setSuccess()
        .setResponse(userRepository.findAll());
  }

  /**
   * <b>  GET /api/secure/users/profile  </b>
   * <br><br>
   * Gets the User object for the authenticated User.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": User
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be an authenticated User to access this endpoint.
   */
  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public UserApiResponse getProfileForAuthenticatedUser(
      @RequestAttribute ApiUser authenticatedUser) {
    return new UserApiResponse()
        .setSuccess()
        .setResponse(authenticatedUser);
  }

  /**
   * <b>  POST /api/secure/users/updateProfile  </b>
   * <br><br>
   * Updates the authenticated user's profile with the following body:
   * <br>
   * <b>REQUEST BODY:</b>
   * <pre>{@code    {
   *    (optional) "email": String,
   *    (optional) "username": String,
   *    (optional) "firstName": String,
   *    (optional) "lastName": String
   * }}</pre>
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": User
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be an authenticated User to access this endpoint.
   */
  @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
  public UserApiResponse updateProfileForAuthenticatedUser(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiUser userUpdate) {
    return new UpdateUserCommand()
        .setUserRepository(userRepository)
        .setActingUser(authenticatedUser)
        .setUserUpdate(userUpdate)
        .execute();
  }

  /**
   * <b>  GET /api/secure/users/forAdmin  </b>
   * <br><br>
   * Gets all the users for this authenticated Admin user. That is to say,
   * gets all the users that belong to this Admin's company.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "users": [User]
   *   }
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be ADMIN or above to access this endpoint.
   */
  @RequestMapping(value = "/forAdmin", method = RequestMethod.GET)
  public UserListApiResponse getUsersForAdmin(@RequestAttribute ApiUser authenticatedUser) {
    checkAccessIsAtLeast(authenticatedUser, AccessLevel.ADMIN);

    return new UsersForAdminCommand()
        .setUserRepository(userRepository)
        .setUser(authenticatedUser)
        .execute();
  }

  /**
   * <b>  GET /api/secure/users/addToLocation/{userId}/{locationId}  </b>
   * <br><br>
   * Adds a User to a location. That is to say, this endpoint gives the User with
   * "userId" permission to access the Location with "locationId". This endpoint
   * is accessible only to SUPER and ADMIN users. If the calling user does not
   * have access to either the User or Location identified by userId and locationId
   * respectively, "ACCESS_DENIED" will be thrown.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": User
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be ADMIN or above to access this endpoint.
   */
  @RequestMapping(value = "/addToLocation/{userId}/{locationId}", method = RequestMethod.GET)
  public UserApiResponse addUserToLocation(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID userId,
      @PathVariable UUID locationId) {
    return new AddUserToLocationCommand()
        .setLocationId(locationId)
        .setUserRepository(userRepository)
        .setLocationRepository(locationRepository)
        .setActingUser(authenticatedUser)
        .setUserId(userId)
        .execute();
  }

  /**
   * <b>  POST /api/secure/users/addToLocation/{userId}/{locationId}  </b>
   * <br><br>
   * Adds a User to multiple location. That is to say, this endpoint gives the User with
   * "userId" permission to access the Locations with ids in the body array "locationIds". This endpoint
   * is accessible only to SUPER and ADMIN users. If the calling user does not
   * have access to either the User or Location identified by userId and locationId
   * respectively, "ACCESS_DENIED" will be thrown.
   * <br><br>
   * <b>  REQUEST BODY:  </b>
   * <pre>{@code    {
   *   "locationIds": [ locationId1, locationId2, ... ]
   * }}</pre>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": User
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be ADMIN or above to access this endpoint.
   */
  @RequestMapping(value = "/addToLocations/{userId}", method = RequestMethod.POST)
  public UserApiResponse addUserToLocations(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID userId,
      @RequestBody ApiLocationIdList locationIdList) {
    return new AddUserToLocationsCommand()
        .setUserRepository(userRepository)
        .setLocationRepository(locationRepository)
        .setActingUser(authenticatedUser)
        .setUserId(userId)
        .setLocationIds(locationIdList.getLocationIds())
        .execute();
  }

  /**
   * <b>  DELETE /api/secure/users/{userId}  </b>
   * <br><br>
   * Deletes the user with id "userId". Only an {Admin|Super} can delete users,
   * and if the user is Admin, they can only delete users at the same company.
   * <br>
   * The result body includes the deleted User object.
   *
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": success,
   *   "response": User
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * User must be ADMIN or above to access this endpoint.
   */
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public UserApiResponse deleteUser(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID userId) {
    return new DeleteUserCommand()
        .setUserRepository(userRepository)
        .setActingUser(authenticatedUser)
        .setUserId(userId)
        .execute();
  }
}
