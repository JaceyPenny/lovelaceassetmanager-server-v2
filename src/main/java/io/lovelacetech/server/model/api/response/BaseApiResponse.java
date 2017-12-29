package io.lovelacetech.server.model.api.response;

import io.lovelacetech.server.model.api.Status;
import io.lovelacetech.server.util.Messages;

public abstract class BaseApiResponse<T extends BaseApiResponse, S> {
  private Status status;
  private String message;

  public T setDefault() {
    setStatus(Status.ERROR);
    setMessage(Messages.DEFAULT);
    return (T) this;
  }

  public T setStatus(Status status) {
    this.status = status;
    return (T) this;
  }

  public Status getStatus() {
    return status;
  }

  public T setMessage(String message) {
    this.message = message;
    return (T) this;
  }

  public String getMessage() {
    return message;
  }

  public abstract S getResponse();
}
