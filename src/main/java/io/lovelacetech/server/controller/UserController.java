package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.user.UpdateUserCommand;
import io.lovelacetech.server.command.user.UsersForAdminCommand;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/secure/users")
public class UserController extends BaseController {

  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
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
   * User must be ADMIN to access this endpoint.
   */
  @RequestMapping(value = "/forAdmin", method = RequestMethod.GET)
  public UserListApiResponse getUsersForAdmin(@RequestAttribute ApiUser authenticatedUser) {
    checkAccessIsAtLeast(authenticatedUser, AccessLevel.ADMIN);

    return new UsersForAdminCommand()
        .setUserRepository(userRepository)
        .setUser(authenticatedUser)
        .execute();
  }
}
