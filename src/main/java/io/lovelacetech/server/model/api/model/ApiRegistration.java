package io.lovelacetech.server.model.api.model;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ApiRegistration extends BaseApiModel {
  private String email;
  private String username;
  private String password;
  private String firstName;
  private String lastName;

  public String getEmail() {
    return email;
  }

  public ApiRegistration setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public ApiRegistration setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public ApiRegistration setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public ApiRegistration setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public ApiRegistration setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public boolean isValid() {
    return !Strings.isNullOrEmpty(email)
        && !Strings.isNullOrEmpty(username)
        && !Strings.isNullOrEmpty(password)
        && !Strings.isNullOrEmpty(firstName)
        && !Strings.isNullOrEmpty(lastName);
  }

  public User createUser(PasswordEncoder encoder) {
    User newUser = new User();
    newUser.setEmail(email);
    newUser.setUsername(username);
    newUser.setPassword(encoder.encode(password));
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setAccessLevel(AccessLevel.USER);

    return newUser;
  }
}
