package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiAuthentication;
import io.lovelacetech.server.model.api.model.ApiAuthenticationResult;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.authentication.AuthenticationApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationController {

  @Autowired
  UserRepository userRepository;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public AuthenticationApiResponse login(@RequestBody ApiAuthentication login) throws ServletException {
    if (login.getUsernameOrEmail() == null || login.getPassword() == null) {
      throw new ServletException(Messages.LOGIN_INVALID_BODY);
    }

    // First, try to find user by email
    User user = userRepository.findByEmail(login.getUsernameOrEmail());
    if (user == null) {
      // Then try to find user by username
      user = userRepository.findByUsername(login.getUsernameOrEmail());
    }

    // No user exists where the username or email is "usernameOrEmail"
    if (user == null) {
      throw new ServletException(Messages.LOGIN_INVALID_CREDENTIALS);
    }

    if (!login.passwordsMatch(user.getPassword())) {
      throw new ServletException(Messages.LOGIN_INVALID_CREDENTIALS);
    }

    ApiUser apiUser = user.toApi();
    String jwt = AuthenticationUtils.jwtSign(apiUser);

    return new AuthenticationApiResponse()
        .setSuccess()
        .setResponse(new ApiAuthenticationResult()
            .setToken(jwt)
            .setUser(apiUser));
  }
}
