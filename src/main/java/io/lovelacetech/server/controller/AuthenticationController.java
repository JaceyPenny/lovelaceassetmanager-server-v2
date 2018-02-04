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
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;

@RestController
@CrossOrigin
@RequestMapping("/api/authenticate")
public class AuthenticationController {

  private final UserRepository userRepository;

  @Autowired
  public AuthenticationController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * <b>  POST /api/authenticate/login  </b>
   * <br><br>
   * Authenticates a user by usernameOrEmail and password.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "user": User,
   *     "token": JWT
   *   }
   * }
   * }
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public AuthenticationApiResponse login(@RequestBody ApiAuthentication login)
      throws ServletException {
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
