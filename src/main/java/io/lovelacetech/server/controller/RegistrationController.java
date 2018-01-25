package io.lovelacetech.server.controller;

import io.lovelacetech.server.LovelaceAssetManagerServerApplication;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiAuthenticationResult;
import io.lovelacetech.server.model.api.model.ApiRegistration;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.authentication.AuthenticationApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(LovelaceAssetManagerServerApplication.ORIGIN_URL)
@RequestMapping("/api/registration")
public class RegistrationController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

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
    User userResult = userRepository.save(newUser);

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
