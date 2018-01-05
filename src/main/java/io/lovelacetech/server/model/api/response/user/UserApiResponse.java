package io.lovelacetech.server.model.api.response.user;

import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class UserApiResponse extends BaseApiResponse<UserApiResponse, ApiUser> {
  @Override
  public UserApiResponse setDefault() {
    super.setDefault();
    setResponse(null);
    return this;
  }

  public UserApiResponse setNotFoundByEmail(String email) {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_USER_FOUND_BY_EMAIL(email));
    setResponse(null);
    return this;
  }

  public UserApiResponse setNotFoundByEmailAndPassword(String email) {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_USER_FOUND_BY_EMAIL_PASSWORD(email));
    setResponse(null);
    return this;
  }
}
