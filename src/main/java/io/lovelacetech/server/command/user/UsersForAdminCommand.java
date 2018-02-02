package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.List;

public class UsersForAdminCommand extends UserCommand<UsersForAdminCommand> {
  private ApiUser user;

  public UsersForAdminCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && user != null;
  }

  @Override
  public UserListApiResponse execute() {
    if (!checkCommand()) {
      return new UserListApiResponse().setDefault();
    }

    List<User> users = getUserRepository().findByCompanyId(user.getCompanyId());

    return new UserListApiResponse()
        .setSuccess()
        .setResponse(users);
  }
}
