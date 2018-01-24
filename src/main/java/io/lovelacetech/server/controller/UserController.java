package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.user.UpdateUserCommand;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/users")
public class UserController extends BaseController {
  @Autowired
  private UserRepository userRepository;

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
}
