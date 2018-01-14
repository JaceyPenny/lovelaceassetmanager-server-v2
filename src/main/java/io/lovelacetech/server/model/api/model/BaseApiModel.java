package io.lovelacetech.server.model.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lovelacetech.server.model.ApiModelConvertible;

import java.beans.Transient;

public class BaseApiModel {
  @Override
  public String toString() {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(this);
  }

  @Transient
  public boolean isValid() {
    return true;
  }

  public <T extends ApiModelConvertible> T toDatabase() {
    return null;
  }
}
