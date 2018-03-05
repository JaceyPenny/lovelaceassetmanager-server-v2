package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;

public class ModifyUserCommand extends UserCommand<ModifyUserCommand> {
  private ApiUser actingUser;
  private ApiUser userUpdate;

  public ModifyUserCommand setActingUser(ApiUser actingUser) {
    this.actingUser = actingUser;
    return this;
  }

  public ModifyUserCommand setUserUpdate(ApiUser userUpdate) {
    this.userUpdate = userUpdate;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && actingUser != null
        && userUpdate != null;
  }

  public UserApiResponse execute() {
    if (!checkCommand()) {
      return new UserApiResponse().setDefault();
    }

    if (!AuthenticationUtils.userIsAtLeast(actingUser, AccessLevel.ADMIN)) {
      return new UserApiResponse().setAccessDenied();
    }

    if (AuthenticationUtils.userIsSuper(userUpdate)
        && !AuthenticationUtils.userIsSuper(actingUser)) {
      return new UserApiResponse().setAccessDenied();
    }

    User databaseUser = userUpdate.toDatabase();

    if (!databaseUser.hasId()) {
      return new UserApiResponse().setInvalidBody();
    }

    User existingUser = getUserRepository().findOne(databaseUser.getId());
    if (existingUser == null) {
      return new UserApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessUser(actingUser, existingUser)) {
      return new UserApiResponse().setAccessDenied();
    }

    existingUser.applyUpdate(databaseUser);

    if (!AccessUtils.userCanAccessUser(actingUser, existingUser)) {
      return new UserApiResponse().setAccessDenied();
    }

    existingUser = getUserRepository().save(existingUser);
    return new UserApiResponse()
        .setSuccess()
        .setResponse(existingUser.toApi());
  }
}
