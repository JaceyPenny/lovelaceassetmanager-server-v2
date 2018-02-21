package io.lovelacetech.server.model.api.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import io.lovelacetech.server.model.api.enums.NotificationType;
import io.lovelacetech.server.model.api.model.ApiNotification;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class ApiNotificationDeserializer extends JsonDeserializer<ApiNotification> {
  @Override
  public ApiNotification deserialize(
      JsonParser jsonParser,
      DeserializationContext context)
      throws IOException {
    ObjectCodec objectCode = jsonParser.getCodec();
    JsonNode jsonNode = objectCode.readTree(jsonParser);

    ApiNotification notification = new ApiNotification();
    if (jsonNode.get("id") != null) {
      notification.setId(UUID.fromString(jsonNode.get("id").asText()));
    }

    if (jsonNode.get("active") != null) {
      notification.setActive(Boolean.valueOf(jsonNode.get("active").asText()));
    }

    if (jsonNode.get("time") != null) {
      DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
      Time time = new Time(0);
      try {
        long timeLong = dateFormat.parse(jsonNode.get("time").asText()).getTime();
        time = new Time(timeLong);
      } catch (ParseException e) {
        // do nothing
      }
      notification.setTime(time);
    }

    if (jsonNode.get("notificationType") != null) {
      notification.setNotificationType(NotificationType.fromString(jsonNode.get("notificationType").asText()));
      if (notification.getNotificationType() == null) {
        throw new IOException("Invalid notificationType: " + jsonNode.get("notificationType").asText());
      }
    }

    JsonNode locationIdsNode = jsonNode.withArray("locationIds");
    List<JsonNode> locationIds = Lists.newArrayList(locationIdsNode.iterator());
    for (JsonNode locationId : locationIds) {
      try {
        notification.addLocationId(UUID.fromString(locationId.asText()));
      } catch (Exception e) {
        // do nothing
      }
    }

    JsonNode deviceIdsNode = jsonNode.withArray("deviceIds");
    List<JsonNode> deviceIds = Lists.newArrayList(deviceIdsNode.iterator());
    for (JsonNode deviceId : deviceIds) {
      try {
        notification.addDeviceId(UUID.fromString(deviceId.asText()));
      } catch (Exception e) {
        // do nothing
      }
    }

    return notification;
  }
}
