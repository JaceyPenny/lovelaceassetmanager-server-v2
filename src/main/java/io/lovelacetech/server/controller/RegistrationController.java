package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiAuthenticationResult;
import io.lovelacetech.server.model.api.model.ApiRegistration;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.authentication.AuthenticationApiResponse;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.LogUtil;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/registration")
public class RegistrationController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final LogRepository logRepository;

  @Autowired
  public RegistrationController(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      LogRepository logRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.logRepository = logRepository;
  }

  /**
   * <b>  POST /api/registration/register  </b>
   * <br><br>
   * Registers a new user. The new user is not a part of a company until invited
   * by an ADMIN user.
   * <br>
   * <b>  REQUEST BODY:  </b><br>
   * <pre>{@code    {
   *   (required) "email": String,
   *   (required) "username": String,
   *   (required) "password": String (plaintext),
   *   (required) "firstName": String,
   *   (required) "lastName": String
   * }}</pre><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "user": User,
   *     "token": JWT
   *    }
   * }}</pre>
   */
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public AuthenticationApiResponse register(@RequestBody ApiRegistration registration) {
    if (!registration.isValid()) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.BAD_REQUEST)
          .setMessage(Messages.INVALID_BODY);
    }

    User existingUserByEmail = userRepository.findByEmail(registration.getEmail());
    if (existingUserByEmail != null) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.CONFLICT)
          .setMessage(Messages.REGISTRATION_EMAIL_ALREADY_EXISTS);
    }

    User existingUserByUsername = userRepository.findByUsername(registration.getUsername());
    if (existingUserByUsername != null) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.CONFLICT)
          .setMessage(Messages.REGISTRATION_USERNAME_ALREADY_EXISTS);
    }

    if (!PasswordUtils.isValidPassword(registration.getPassword())) {
      return new AuthenticationApiResponse()
          .setStatus(HttpStatus.PRECONDITION_FAILED)
          .setMessage(Messages.REGISTRATION_PASSWORD_INVALID);
    }

    User newUser = registration.createUser(passwordEncoder);
    User userResult = LogUtil.registerUserAndLog(newUser.toApi(), userRepository, logRepository);

    if (userResult == null) {
      return new AuthenticationApiResponse()
          .setDefault();
    }

    ApiUser apiUser = userResult.toApi();
    String jwt = AuthenticationUtils.jwtSign(apiUser);

    return new AuthenticationApiResponse()
        .setSuccess()
        .setResponse(new ApiAuthenticationResult()
            .setToken(jwt)
            .setUser(apiUser));
  }
}
