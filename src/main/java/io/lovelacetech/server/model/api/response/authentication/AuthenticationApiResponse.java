package io.lovelacetech.server.model.api.response.authentication;

import io.lovelacetech.server.model.api.model.ApiToken;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class AuthenticationApiResponse extends BaseApiResponse<AuthenticationApiResponse, ApiToken> {
  @Override
  public AuthenticationApiResponse setDefault() {
    super.setDefault();
    setResponse(null);
    return this;
  }
}
