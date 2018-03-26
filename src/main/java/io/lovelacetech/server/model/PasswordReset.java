package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiPasswordReset;
import io.lovelacetech.server.model.converter.LocalDateTimeConverter;
import io.lovelacetech.server.util.UUIDUtils;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset", schema = "lovelace")
public class PasswordReset implements DatabaseModel<PasswordReset>, ApiModelConvertible<ApiPasswordReset> {

  @Id
  @GeneratedValue
  @ColumnDefault("uuid_generate_v4()")
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", updatable = false, nullable = false)
  private User user;

  @Column(name = "code", updatable = false, nullable = false)
  private String code;

  @Convert(converter = LocalDateTimeConverter.class)
  @Column(name = "expiration", updatable = false, nullable = false)
  private LocalDateTime expiration;

  @Override
  public ApiPasswordReset toApi() {
    return new ApiPasswordReset(this);
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
  public void applyUpdate(PasswordReset other) {
    // No updates allowed on PasswordReset objects
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

  public LocalDateTime getExpiration() {
    return expiration;
  }

  public void setExpiration(LocalDateTime expiration) {
    this.expiration = expiration;
  }
}
