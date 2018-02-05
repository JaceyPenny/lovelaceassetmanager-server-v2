package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class DeleteUserCommand extends UserCommand<DeleteUserCommand> {
  private UUID userId;
  private ApiUser actingUser;

  public DeleteUserCommand setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public DeleteUserCommand setActingUser(ApiUser actingUser) {
    this.actingUser = actingUser;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && UUIDUtils.isValidId(userId)
        && actingUser != null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public UserApiResponse execute() {
    if (!checkCommand()) {
      return new UserApiResponse().setDefault();
    }

    if (AuthenticationUtils.userIsAtLeast(actingUser, AccessLevel.ADMIN)) {
      return new UserApiResponse().setAccessDenied();
    }

    User user = getUserRepository().findOne(userId);
    if (user == null) {
      return new UserApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessUser(actingUser, user)) {
      return new UserApiResponse().setAccessDenied();
    }

    getUserRepository().delete(userId);
    return new UserApiResponse()
        .setSuccess()
        .setResponse(user.toApi());
  }
}
