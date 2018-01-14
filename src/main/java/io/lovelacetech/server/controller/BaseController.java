package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.util.Messages;
import org.springframework.security.access.AccessDeniedException;

public class BaseController {

  protected void checkAccess(ApiUser user, AccessLevel accessLevel) {
    if (user.getAccessLevel().toInt() < accessLevel.toInt()) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }
  }

  protected void checkIsSuper(ApiUser user) {
    checkAccess(user, AccessLevel.SUPER);
  }
}
