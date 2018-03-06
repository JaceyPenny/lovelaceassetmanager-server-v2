package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.util.UUIDUtils;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "invite", schema = "lovelace")
public class Invite implements DatabaseModel<Invite>, ApiModelConvertible<ApiInvite> {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", updatable = false, nullable = false)
  private User user;

  @Column(name = "code", unique = true, nullable = false, updatable = false)
  private String code;

  @Column(name = "email", nullable = false, updatable = false)
  private String email;

  @Override
  public ApiInvite toApi() {
    return new ApiInvite(this);
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean hasId() {
    return UUIDUtils.isValidId(id);
  }

  @Override
  public boolean idEquals(UUID otherId) {
    return UUIDUtils.idsEqual(id, otherId);
  }

  @Override
  public void applyUpdate(Invite other) {
    // do nothing
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
