package io.lovelacetech.server.model.api;

public enum Status {
  SUCCESS("success"),
  NOT_FOUND("not_found"),
  ERROR("error");

  private final String value;
  Status(String value) {
    this.value = value;
  }
}
