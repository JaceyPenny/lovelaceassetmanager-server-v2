package io.lovelacetech.server.model.api.response.registration;

import io.lovelacetech.server.model.api.model.ApiAuthenticationResult;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class RegistrationApiResponse extends BaseApiResponse<RegistrationApiResponse, ApiAuthenticationResult> {
  @Override
  public RegistrationApiResponse setDefault() {
    super.setDefault();
    setResponse(null);
    return this;
  }
}
