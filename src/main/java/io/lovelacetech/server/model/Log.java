package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.enums.LogType;
import io.lovelacetech.server.model.api.model.ApiLog;
import io.lovelacetech.server.model.converter.JsonbConverter;
import io.lovelacetech.server.model.converter.LocalDateTimeConverter;
import io.lovelacetech.server.model.converter.LogTypeConverter;
import io.lovelacetech.server.util.UUIDUtils;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "log", schema = "lovelace")
public class Log implements DatabaseModel<Log>, ApiModelConvertible<ApiLog> {
  @Id
  @GeneratedValue
  @ColumnDefault("uuid_generate_v4()")
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Convert(converter = LogTypeConverter.class)
  @Column(name = "type", nullable = false, updatable = false)
  private LogType type;

  @Convert(converter = LocalDateTimeConverter.class)
  @Column(name = "timestamp", nullable = false)
  private LocalDateTime timestamp;

  @Column(name = "object_id", updatable = false)
  private UUID objectId;

  @Column(name = "user_id", updatable = false)
  private UUID userId;

  @Convert(converter = JsonbConverter.class)
  @Column(name = "old_data", updatable = false)
  private Map<String, Object> oldData;

  @Convert(converter = JsonbConverter.class)
  @Column(name = "new_data", updatable = false)
  private Map<String, Object> newData;

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

  public LogType getType() {
    return type;
  }

  public void setType(LogType type) {
    this.type = type;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public UUID getObjectId() {
    return objectId;
  }

  public void setObjectId(UUID objectId) {
    this.objectId = objectId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public Map<String, Object> getOldData() {
    return oldData;
  }

  public void setOldData(Map<String, Object> oldData) {
    this.oldData = oldData;
  }

  public Map<String, Object> getNewData() {
    return newData;
  }

  public void setNewData(Map<String, Object> newData) {
    this.newData = newData;
  }
}
