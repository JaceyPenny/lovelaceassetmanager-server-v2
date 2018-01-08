package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.util.UUIDUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class ApiLog extends BaseApiModel {

  private UUID id;
  private UUID assetId;
  private UUID fromId;
  private UUID toId;
  private LocalDateTime timestamp;

  public ApiLog() {
    id = UUIDUtils.empty();
    assetId = UUIDUtils.empty();
    fromId = UUIDUtils.empty();
    toId = UUIDUtils.empty();
    timestamp = LocalDateTime.now();
  }

  public ApiLog(Log log) {
    id = log.getId();
    assetId = log.getAssetId();
    fromId = log.getFromId();
    toId = log.getFromId();
    timestamp = log.getTimestamp();
  }

  public UUID getId() {
    return id;
  }

  public ApiLog setId(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getAssetId() {
    return assetId;
  }

  public ApiLog setAssetId(UUID assetId) {
    this.assetId = assetId;
    return this;
  }

  public UUID getFromId() {
    return fromId;
  }

  public ApiLog setFromId(UUID fromId) {
    this.fromId = fromId;
    return this;
  }

  public UUID getToId() {
    return toId;
  }

  public ApiLog setToId(UUID toId) {
    this.toId = toId;
    return this;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public ApiLog setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  @Override
  public Log toDatabase() {
    Log log = new Log();

    log.setId(id);
    log.setAssetId(assetId);
    log.setFromId(fromId);
    log.setToId(toId);
    log.setTimestamp(timestamp);

    return log;
  }
}
