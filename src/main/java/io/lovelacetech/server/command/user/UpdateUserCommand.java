package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import org.h2.util.StringUtils;

public class UpdateUserCommand extends UserCommand<UpdateUserCommand> {
  private ApiUser actingUser;
  private ApiUser userUpdate;

  public UpdateUserCommand setActingUser(ApiUser actingUser) {
    this.actingUser = actingUser;
    return this;
  }

  public UpdateUserCommand setUserUpdate(ApiUser userUpdate) {
    this.userUpdate = userUpdate;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && actingUser != null && userUpdate != null;
  }

  @Override
  public UserApiResponse execute() {
    if (!checkCommand()) {
      return new UserApiResponse().setDefault();
    }

    User databaseUser = actingUser.toDatabase();

    if (!StringUtils.isNullOrEmpty(userUpdate.getEmail())) {
      databaseUser.setEmail(userUpdate.getEmail());
    }

    if (!StringUtils.isNullOrEmpty(userUpdate.getUsername())) {
      databaseUser.setUsername(userUpdate.getUsername());
    }

    if (!StringUtils.isNullOrEmpty(userUpdate.getFirstName())) {
      databaseUser.setFirstName(userUpdate.getFirstName());
    }

    if (!StringUtils.isNullOrEmpty(userUpdate.getLastName())) {
      databaseUser.setLastName(userUpdate.getLastName());
    }

    databaseUser = getUserRepository().save(databaseUser);

    return new UserApiResponse()
        .setSuccess()
        .setResponse(databaseUser.toApi());
  }
}
