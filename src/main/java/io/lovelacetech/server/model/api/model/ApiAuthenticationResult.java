package io.lovelacetech.server.model.api.model;

public class ApiAuthenticationResult extends BaseApiModel {
  private ApiUser user;
  private String token;


  public ApiUser getUser() {
    return user;
  }

  public ApiAuthenticationResult setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public String getToken() {
    return token;
  }

  public ApiAuthenticationResult setToken(String token) {
    this.token = token;
    return this;
  }
}
