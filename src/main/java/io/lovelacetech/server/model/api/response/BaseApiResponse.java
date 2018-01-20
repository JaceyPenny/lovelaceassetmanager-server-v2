package io.lovelacetech.server.model.api.response;

import io.lovelacetech.server.model.api.model.BaseApiModel;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

import java.beans.Transient;

public abstract class BaseApiResponse<T extends BaseApiResponse, S extends BaseApiModel> {
  private int status;
  private String message;
  private S response;

  public T setDefault() {
    setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    setMessage(Messages.DEFAULT);
    response = null;
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

  public T setAccessDenied() {
    setStatus(HttpStatus.FORBIDDEN);
    setMessage(Messages.ACCESS_DENIED);
    return (T) this;
  }

  public T setInvalidBody() {
    setStatus(HttpStatus.BAD_REQUEST);
    setMessage(Messages.INVALID_BODY);
    return (T) this;
  }

  public T setConflict() {
    setStatus(HttpStatus.CONFLICT);
    setMessage(Messages.CONFLICT);
    return (T) this;
  }

  public T setCannotModify() {
    setStatus(HttpStatus.NOT_ACCEPTABLE);
    setMessage(Messages.CANNOT_MODIFY);
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

  @Transient
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
