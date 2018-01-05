package io.lovelacetech.server.model.api.model;

public class ApiToken extends BaseApiModel {
  private String token;

  public ApiToken setToken(String token) {
    this.token = token;
    return this;
  }

  public String getToken() {
    return token;
  }
}
