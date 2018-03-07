package io.lovelacetech.server.model.api.model;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.Invite;

import java.util.UUID;

public class ApiInvite extends BaseApiModel<Invite> {

  private UUID id;
  private ApiUser user;
  private String code;
  private String email;

  public ApiInvite() {

  }

  public ApiInvite(Invite invite) {
    id = invite.getId();
    user = invite.getUser().toApi();
    code = invite.getCode();
    email = invite.getEmail();
  }

  public UUID getId() {
    return id;
  }

  public ApiInvite setId(UUID id) {
    this.id = id;
    return this;
  }

  public ApiUser getUser() {
    return user;
  }

  public ApiInvite setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public String getCode() {
    return code;
  }

  public ApiInvite setCode(String code) {
    this.code = code;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public ApiInvite setEmail(String email) {
    this.email = email;
    return this;
  }

  @Override
  public boolean isValid() {
    return user.isValid()
        && !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(email);
  }

  @Override
  public Invite toDatabase() {
    Invite newInvite = new Invite();

    newInvite.setId(id);
    newInvite.setUser(user.toDatabase());
    newInvite.setCode(code);
    newInvite.setEmail(email);

    return newInvite;
  }
}
