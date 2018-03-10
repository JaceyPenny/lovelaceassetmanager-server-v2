package io.lovelacetech.server.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TwilioService {

  private String sourcePhoneNumber;

  public void setSourcePhoneNumber(String sourcePhoneNumber) {
    this.sourcePhoneNumber = sourcePhoneNumber;
  }

  public MessageCreator getMessageForNotification(ApiNotification notification, ApiUser user) {
    String notificationText = getNotificationText(notification, user);
    String userPhoneNumber = user.getPhoneNumber();

    if (userPhoneNumber == null) {
      return null;
    }

    return Message.creator(
        new PhoneNumber(userPhoneNumber),
        new PhoneNumber(sourcePhoneNumber),
        notificationText);
  }

  private String getNotificationText(ApiNotification notification, ApiUser user) {
    StringBuilder builder = new StringBuilder();

    builder.append("Hey, ").append(user.getFirstName()).append(", it's Lovelace Technologies.");
    builder.append('\n');
    builder.append("We've got an inventory update for you:\n");

    Map<ApiDevice, Integer> devicesWithMissingAssets = notification.getLocations().stream()
        .flatMap((location) -> location.getDevices().stream())
        .collect(Collectors.toMap(
            Function.identity(),
            (device) -> device.getAssetsWithHome()
                - (int) device.getAssets().stream().filter(
                (asset) -> asset.getHomeId().equals(device.getId()))
                .count()
        ));

    List<ApiDevice> missingDevices = new ArrayList<>();
    int missingAssets = 0;

    for (ApiDevice device : devicesWithMissingAssets.keySet()) {
      if (devicesWithMissingAssets.get(device) != 0) {
        missingDevices.add(device);
        missingAssets += devicesWithMissingAssets.get(device);
      }
    }

    if (missingAssets > 0) {
      builder.append(String.format(
          "%d missing assets from these devices: %s\n\n",
          missingAssets, deviceNames(missingDevices)));
    } else {
      builder.append("No missing assets.\n\n");
    }
    builder.append("Go to https://dashboard.lovelacetech.io/ to see your current inventory.");
    return builder.toString();
  }

  private String deviceNames(List<ApiDevice> devices) {
    return Strings
        .join(devices.stream().map(ApiDevice::getName).collect(Collectors.toList()))
        .with(", ");
  }
}
