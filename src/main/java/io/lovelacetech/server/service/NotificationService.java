package io.lovelacetech.server.service;

import com.sendgrid.*;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.*;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
  private final SendGrid sendGrid;
  private final MailContentBuilder mailContentBuilder;
  private final EmailBuilder emailBuilder;

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  public NotificationService(
      SendGrid sendGrid,
      MailContentBuilder mailContentBuilder,
      EmailBuilder emailBuilder,
      UserRepository userRepository,
      NotificationRepository notificationRepository,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    this.sendGrid = sendGrid;
    this.mailContentBuilder = mailContentBuilder;
    this.emailBuilder = emailBuilder;
    this.userRepository = userRepository;
    this.notificationRepository = notificationRepository;
    this.locationRepository = locationRepository;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
  }

  @Scheduled(fixedRate = 60000) // fixedRate = 1 minute
  public void checkAndSendNotifications() {
    List<ApiNotification> notifications = getCurrentNotifications();
    for (ApiNotification notification : notifications) {
      sendNotification(notification);
    }
  }

  private List<ApiNotification> getCurrentNotifications() {
    Time currentTime = Time.valueOf(LocalTime.now().withSecond(0));
    List<ApiNotification> notifications =
        RepositoryUtils.toApiList(notificationRepository.findByTimeEquals(currentTime));
    return notifications.stream().filter(ApiNotification::isActive).collect(Collectors.toList());
  }

  private void sendNotification(ApiNotification notification) {
    LoaderUtils.populateNotification(
        notification, locationRepository, deviceRepository, assetRepository, logRepository);

    User fetchedUser = userRepository.findOne(notification.getUserId());
    if (fetchedUser == null) {
      return;
    }

    ApiUser user = fetchedUser.toApi();
    String html = mailContentBuilder.buildNotification(user, notification);
    Mail mail = emailBuilder.buildMail(
        "notification@lovelacetech.io",
        "Lovelace Technologies Notifications",
        user.getFirstName() + ", Your Daily Inventory Report",
        user.getEmail(),
        html);

    try {
      Request request = emailBuilder.buildRequest(mail);
      sendGrid.api(request);
    } catch (IOException ex) {
      // do nothing
    }
  }
}
