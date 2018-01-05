package io.lovelacetech.server.model.api.response.user;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUserList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class UserListApiResponse extends BaseApiResponse<UserListApiResponse, ApiUserList> {
  @Override
  public UserListApiResponse setDefault() {
    super.setDefault();
    super.setResponse(null);
    return this;
  }

  public UserListApiResponse setNotFound() {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_USERS_FOUND);
    super.setResponse(null);
    return this;
  }

  public UserListApiResponse setResponse(Iterable<User> users) {
    setResponse(new ApiUserList(users));
    return this;
  }
}
