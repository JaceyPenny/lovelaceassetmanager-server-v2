package io.lovelacetech.server.model.api.response.user;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.model.api.model.ApiUserList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;

public class UserListApiResponse extends BaseApiResponse<UserListApiResponse, ApiUserList> {
  private ApiUserList userList;

  @Override
  public UserListApiResponse setDefault() {
    super.setDefault();
    userList = null;
    return this;
  }

  public UserListApiResponse setNotFound() {
    setStatus(Status.NOT_FOUND);
    setMessage(Messages.NO_USERS_FOUND);
    this.userList = null;
    return this;
  }

  public UserListApiResponse setResponse(Iterable<User> users) {
    setResponse(new ApiUserList(users));
    return this;
  }

  @Override
  public UserListApiResponse setResponse(ApiUserList value) {
    this.userList = value;
    return this;
  }

  @Override
  public ApiUserList getResponse() {
    return userList;
  }
}
