package io.lovelacetech.server.model.api.response.authentication;

import io.lovelacetech.server.model.api.model.ApiAuthenticationResult;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class AuthenticationApiResponse extends BaseApiResponse<AuthenticationApiResponse, ApiAuthenticationResult> {
  @Override
  public AuthenticationApiResponse setDefault() {
    super.setDefault();
    setResponse(null);
    return this;
  }
}
