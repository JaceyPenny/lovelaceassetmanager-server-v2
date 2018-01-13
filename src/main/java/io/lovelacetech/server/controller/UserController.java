package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;

@RestController
@RequestMapping("/api/secure/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public UserListApiResponse getUsers(@RequestAttribute ApiUser authenticatedUser) {
    if (!AuthenticationUtils.userIsSuper(authenticatedUser)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }

    return new UserListApiResponse()
        .setSuccess()
        .setResponse(userRepository.findAll());
  }
}
