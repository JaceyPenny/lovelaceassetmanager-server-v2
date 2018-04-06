package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.Invite;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.invite.InviteApiResponse;
import io.lovelacetech.server.repository.InviteRepository;
import io.lovelacetech.server.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class InviteController extends BaseController {

  private final InviteService inviteService;
  private final InviteRepository inviteRepository;

  @Autowired
  public InviteController(InviteService inviteService, InviteRepository inviteRepository) {
    this.inviteService = inviteService;
    this.inviteRepository = inviteRepository;
  }

  @GetMapping(value = "/api/invite/{inviteCode}")
  public InviteApiResponse getInvite(@PathVariable String inviteCode) {
    Invite invite = inviteRepository.findByCode(inviteCode);

    if (invite == null) {
      return new InviteApiResponse().setNotFound();
    } else {
      ApiInvite apiInvite = invite.toApi();

      // sanitize the invite
      apiInvite.getUser().setPhoneNumber("");
      apiInvite.getUser().setAccessLevel(null);
      apiInvite.getUser().setId(null);
      apiInvite.getUser().setLocations(null);

      return new InviteApiResponse().setSuccess().setResponse(apiInvite);
    }
  }

  /**
   * <b> PUT /api/secure/invite </b>
   * <br>Invites a new user by their email, supplied in the PUT body.
   * <br><br><b>  REQUEST BODY:  </b>
   * <pre>{@code {
   *   "email": String
   * }}</pre>
   * <br><br><b>  RESPONSE:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": Invite
   * }}</pre><br><br>
   * <b>  PERMISSIONS:  </b>
   * User must be an authenticated ADMIN.
   */
  @PutMapping(value = "/api/secure/invite")
  public InviteApiResponse inviteUser(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody Map<String, String> body) {
    checkAccessIsAtLeast(authenticatedUser, AccessLevel.ADMIN);

    if (body.get("email") == null) {
      return new InviteApiResponse().setInvalidBody();
    }

    ApiInvite invite = inviteService.inviteByEmail(authenticatedUser, body.get("email"));
    if (invite == null) {
      return new InviteApiResponse().setInvalidBody();
    }

    return new InviteApiResponse()
        .setSuccess()
        .setResponse(invite);
  }
}
