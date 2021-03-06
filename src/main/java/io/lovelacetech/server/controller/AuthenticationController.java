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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;

/**
 * The API controller for approving login credentials and issuing JWT's
 */
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
   * <b>  REQUEST BODY:</b>
   * <pre>{@code {
   *    (required) "usernameOrEmail": String,
   *    (required) "password": String
   * }}</pre>
   * <b>  RESULT:  </b><br>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "user": User,
   *     "token": JWT
   *    }
   * }}</pre>
   *
   * @param login Authentication
   * @return Authentication (with token)
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public AuthenticationApiResponse login(@RequestBody ApiAuthentication login)
      throws ServletException {
    if (login.getUsernameOrEmail() == null || login.getPassword() == null) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.BAD_REQUEST)
          .setMessage(Messages.LOGIN_INVALID_BODY);
    }

    // First, try to find user by email
    User user = userRepository.findByEmail(login.getUsernameOrEmail());
    if (user == null) {
      // Then try to find user by username
      user = userRepository.findByUsername(login.getUsernameOrEmail());
    }

    // No user exists where the username or email is "usernameOrEmail"
    if (user == null) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.UNAUTHORIZED)
          .setMessage(Messages.LOGIN_INVALID_CREDENTIALS);
    }

    if (!login.passwordsMatch(user.getPassword())) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.UNAUTHORIZED)
          .setMessage(Messages.LOGIN_INVALID_CREDENTIALS);
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
