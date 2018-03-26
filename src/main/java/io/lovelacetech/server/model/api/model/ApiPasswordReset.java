package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.PasswordReset;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.UUID;

public class ApiPasswordReset extends BaseApiModel<PasswordReset> {

  private UUID id;
  private ApiUser user;
  private String code;
  private LocalDateTime expiration;

  public ApiPasswordReset() {

  }

  public ApiPasswordReset(PasswordReset passwordReset) {
    this.id = passwordReset.getId();
    this.user = passwordReset.getUser().toApi();
    this.code = passwordReset.getCode();
    this.expiration = passwordReset.getExpiration();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ApiUser getUser() {
    return user;
  }

  public void setUser(ApiUser user) {
    this.user = user;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Transient
  public LocalDateTime getExpiration() {
    return expiration;
  }

  public void setExpiration(LocalDateTime expiration) {
    this.expiration = expiration;
  }
}
