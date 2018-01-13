package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiLog;
import io.lovelacetech.server.model.converter.LocalDateTimeConverter;
import io.lovelacetech.server.util.UUIDUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log", schema = "lovelace")
public class Log implements DatabaseModel<Log>, ApiModelConvertible<ApiLog> {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "asset_id", nullable = false)
  private UUID assetId;

  @Column(name = "from_id", nullable = false)
  private UUID fromId;

  @Column(name = "to_id", nullable = false)
  private UUID toId;

  @Convert(converter = LocalDateTimeConverter.class)
  @Column(name = "timestamp", nullable = false)
  private LocalDateTime timestamp;

  @Override
  public ApiLog toApi() {
    return new ApiLog(this);
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
  public void applyUpdate(Log other) {
    // Do nothing, Logs should not be updated
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getAssetId() {
    return assetId;
  }

  public void setAssetId(UUID assetId) {
    this.assetId = assetId;
  }

  public UUID getFromId() {
    return fromId;
  }

  public void setFromId(UUID fromId) {
    this.fromId = fromId;
  }

  public UUID getToId() {
    return toId;
  }

  public void setToId(UUID toId) {
    this.toId = toId;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
