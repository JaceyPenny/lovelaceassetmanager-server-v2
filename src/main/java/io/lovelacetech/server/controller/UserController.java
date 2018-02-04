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

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public UserListApiResponse getUsers(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new UserListApiResponse()
        .setSuccess()
        .setResponse(userRepository.findAll());
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public UserApiResponse getProfileForAuthenticatedUser(
      @RequestAttribute ApiUser authenticatedUser) {
    return new UserApiResponse()
        .setSuccess()
        .setResponse(authenticatedUser);
  }

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

  @RequestMapping(value = "/forAdmin", method = RequestMethod.GET)
  public UserListApiResponse getUsersForAdmin(@RequestAttribute ApiUser authenticatedUser) {
    checkAccess(authenticatedUser, AccessLevel.ADMIN);

    return new UsersForAdminCommand()
        .setUserRepository(userRepository)
        .setUser(authenticatedUser)
        .execute();
  }
}
