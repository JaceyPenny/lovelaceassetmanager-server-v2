package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.invite.InviteApiResponse;
import io.lovelacetech.server.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/secure/invite")
public class InviteController extends BaseController {

  private final InviteService inviteService;

  @Autowired
  public InviteController(InviteService inviteService) {
    this.inviteService = inviteService;
  }

  @RequestMapping(value = "/{email}", method = RequestMethod.PUT)
  public InviteApiResponse inviteUser(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String email) {
    checkAccessIsAtLeast(authenticatedUser, AccessLevel.ADMIN);

    ApiInvite invite = inviteService.inviteByEmail(authenticatedUser, email);
    if (invite == null) {
      return new InviteApiResponse().setInvalidBody();
    }

    return new InviteApiResponse()
        .setSuccess()
        .setResponse(invite);
  }
}
