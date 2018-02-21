package io.lovelacetech.server.service;

import io.lovelacetech.server.model.api.enums.UpdateDeviceResponse;
import io.lovelacetech.server.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UpdateDeviceValidationService {
  @Value("${UPDATE_DEVICE_SECRET}")
  private String updateDeviceSecret;

  @Value("${LOVELACE_DEBUG}")
  private boolean debug;

  public UpdateDeviceResponse checkRequestValidity(String url, String deviceCode, long timestamp, String hash) {
    if (debug) {
      return UpdateDeviceResponse.SUCCESS;
    }

    long currentEpochSecond = Calendar.getInstance().toInstant().getEpochSecond();

    if (Math.abs(currentEpochSecond - timestamp) > 300) { // check if timestamp is within 5 minutes
      return UpdateDeviceResponse.INVALID_TIMESTAMP;
    }

    String preHashString = url.replaceAll("/", "") + "/" + deviceCode + updateDeviceSecret + timestamp;
    String hashedString = HashUtil.getSha256HashString(preHashString);

    return hashedString.equalsIgnoreCase(hash) ?
        UpdateDeviceResponse.SUCCESS :
        UpdateDeviceResponse.INVALID_SECRET;
  }
}
