package io.lovelacetech.server.model.api.response.user;

import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;

public class UserApiResponse extends BaseApiResponse<UserApiResponse, ApiUser> {
  private ApiUser user;

  @Override
  public UserApiResponse setDefault() {
    super.setDefault();
    user = null;
    return this;
  }

  public UserApiResponse setNotFoundByEmail(String email) {
    setStatus(Status.NOT_FOUND);
    setMessage(Messages.NO_USER_FOUND_BY_EMAIL(email));
    setResponse(null);
    return this;
  }

  public UserApiResponse setNotFoundByEmailAndPassword(String email) {
    setStatus(Status.NOT_FOUND);
    setMessage(Messages.NO_USER_FOUND_BY_EMAIL_PASSWORD(email));
    setResponse(null);
    return this;
  }

  @Override
  public UserApiResponse setResponse(ApiUser user) {
    this.user = user;
    return this;
  }

  @Override
  public ApiUser getResponse() {
    return user;
  }
}
