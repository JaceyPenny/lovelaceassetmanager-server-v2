package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public UserListApiResponse getUsers() {

    Iterable<User> users = userRepository.findAll();

    return new UserListApiResponse()
        .setSuccess()
        .setResponse(users);
  }
}
