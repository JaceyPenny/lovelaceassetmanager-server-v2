package io.lovelacetech.server.model.api.response;

import io.lovelacetech.server.model.api.model.BaseApiModel;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public abstract class BaseApiResponse<T extends BaseApiResponse, S extends BaseApiModel> {
  private int status;
  private String message;
  private S response;

  public T setDefault() {
    setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    setMessage(Messages.DEFAULT);
    return (T) this;
  }

  public T setSuccess() {
    setStatus(HttpStatus.OK);
    setMessage(Messages.SUCCESS);
    return (T) this;
  }

  public T setNotFound() {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NOT_FOUND);
    return (T) this;
  }

  public T setStatus(HttpStatus status) {
    setStatus(status.value());
    return (T) this;
  }

  public T setStatus(int status) {
    this.status = status;
    return (T) this;
  }

  public int getStatus() {
    return status;
  }

  public HttpStatus getHttpStatus() {
    return HttpStatus.valueOf(status);
  }

  public T setMessage(String message) {
    this.message = message;
    return (T) this;
  }

  public String getMessage() {
    return message;
  }

  public T setResponse(S response) {
    this.response = response;
    return (T) this;
  }
  public S getResponse() {
    return response;
  }
}
