package io.lovelacetech.server.model.api.model;

import java.util.ArrayList;
import java.util.List;

public class ApiDeviceUpdate extends BaseApiModel {
  private List<String> rfidTags;

  public ApiDeviceUpdate() {
    rfidTags = new ArrayList<>();
  }

  public ApiDeviceUpdate(List<String> rfidTags) {
    this.rfidTags = rfidTags;
  }

  public List<String> getRfidTags() {
    return rfidTags;
  }

  public ApiDeviceUpdate setRfidTags(List<String> rfidTags) {
    this.rfidTags = rfidTags;
    return this;
  }
}
