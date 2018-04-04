package io.lovelacetech.server.model.api.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.enums.LogType;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonPropertyOrder({"id", "type", "utcTime", "objectId", "userId", "oldData", "newData"})
public class ApiLog extends BaseApiModel<Log> {

  private UUID id;
  private LogType type;
  private LocalDateTime timestamp;
  private UUID objectId;
  private UUID userId;
  private String userFullName;
  private Map<String, Object> oldData;
  private Map<String, Object> newData;

  public ApiLog() {
    id = null;
    type = LogType.ASSET_MOVED;
    timestamp = LocalDateTime.now();
    userId = null;
    oldData = new HashMap<>();
    newData = new HashMap<>();
  }

  public ApiLog(Log log) {
    id = log.getId();
    type = log.getType();
    timestamp = log.getTimestamp();
    objectId = log.getObjectId();
    userId = log.getUserId();
    oldData = log.getOldData();
    newData = log.getNewData();
  }

  public UUID getId() {
    return id;
  }

  public ApiLog setId(UUID id) {
    this.id = id;
    return this;
  }

  public LogType getType() {
    return type;
  }

  public void setType(LogType type) {
    this.type = type;
  }

  @Transient
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public long getUtcTime() {
    return timestamp.toInstant(ZoneOffset.UTC).getEpochSecond();
  }

  public ApiLog setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public UUID getObjectId() {
    return objectId;
  }

  public ApiLog setObjectId(UUID objectId) {
    this.objectId = objectId;
    return this;
  }

  public UUID getUserId() {
    return userId;
  }

  public ApiLog setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public Map<String, Object> getOldData() {
    return oldData;
  }

  public ApiLog setOldData(Map<String, Object> oldData) {
    this.oldData = oldData;
    return this;
  }

  public Map<String, Object> getNewData() {
    return newData;
  }

  public ApiLog setNewData(Map<String, Object> newData) {
    this.newData = newData;
    return this;
  }

  public String getFormattedTime() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("h:mma; M/dd/yy");
    return getTimestamp().atZone(ZoneId.of("UTC-06:00")).format(dateTimeFormatter);
  }

  @Override
  public Log toDatabase() {
    Log log = new Log();

    log.setId(id);
    log.setType(type);
    log.setTimestamp(timestamp);
    log.setObjectId(objectId);
    log.setUserId(userId);
    log.setOldData(oldData);
    log.setNewData(newData);

    return log;
  }

  public String getUserFullName() {
    return userFullName;
  }

  public void setUserFullName(String userFullName) {
    this.userFullName = userFullName;
  }
}
