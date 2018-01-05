package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.util.Messages;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletException;

public class ApiAuthentication extends BaseApiModel {
  private String usernameOrEmail;
  private String password;

  public ApiAuthentication setUsernameOrEmail(String usernameOrEmail) {
    this.usernameOrEmail = usernameOrEmail;
    return this;
  }

  public String getUsernameOrEmail() {
    return usernameOrEmail;
  }

  public ApiAuthentication setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public boolean passwordsMatch(String storedPassword) throws ServletException {
    if (this.password == null) {
      throw new ServletException(Messages.LOGIN_INVALID_BODY);
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.matches(this.password, storedPassword);
  }
}
