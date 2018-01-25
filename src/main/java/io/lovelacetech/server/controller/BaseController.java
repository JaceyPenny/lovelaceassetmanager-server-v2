package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.UUIDUtils;
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

  protected void checkBelongsToCompany(ApiUser user) {
    if (!AuthenticationUtils.userIsSuper(user) && !UUIDUtils.isValidId(user.getCompanyId())) {
      throw new AccessDeniedException(Messages.USER_DOES_NOT_BELONG_TO_COMPANY);
    }
  }
}
