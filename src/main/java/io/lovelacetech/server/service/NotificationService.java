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

@Service
public class NotificationService {

  private final SendGrid sendGrid;
  private final MailContentBuilder mailContentBuilder;

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  public NotificationService(
      SendGrid sendGrid,
      MailContentBuilder mailContentBuilder,
      UserRepository userRepository,
      NotificationRepository notificationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    this.sendGrid = sendGrid;
    this.mailContentBuilder = mailContentBuilder;
    this.userRepository = userRepository;
    this.notificationRepository = notificationRepository;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
  }

  @Scheduled(fixedRate = 60000) // fixedRate = 1 minute
  public void checkAndSendNotifications() {
    List<ApiNotification> notifications = getCurrentNotifications();
    System.out.println(notifications.size());
    for (ApiNotification notification : notifications) {
      sendNotification(notification);
    }
  }

  private List<ApiNotification> getCurrentNotifications() {
    Time currentTime = Time.valueOf(LocalTime.now().withSecond(0));
    return RepositoryUtils.toApiList(notificationRepository.findByTimeEquals(currentTime));
  }

  public void sendNotification(ApiNotification notification) {
    LoaderUtils.populateNotification(
        notification, deviceRepository, assetRepository, logRepository);

    User fetchedUser = userRepository.findOne(notification.getUserId());
    if (fetchedUser == null) {
      return;
    }

    ApiUser user = fetchedUser.toApi();
    Mail notificationMail = makeMail(user, notification);

    try {
      Request request = buildSendMailRequest(notificationMail);
      sendGrid.api(request);
    } catch (IOException ex) {
      // do nothing
    }
  }

  private Mail makeMail(ApiUser user, ApiNotification notification) {
    Email from = new Email("notification@lovelacetech.io");
    from.setName("Lovelace Technologies Notifications");
    String subject = user.getFirstName() + ", Your Daily Inventory Report";
    Email to = new Email(user.getEmail());

    String html = mailContentBuilder.build(user, notification);

    Content content = new Content("text/html", html);

    return new Mail(from, subject, to, content);
  }

  private Request buildSendMailRequest(Mail mail) throws IOException {
    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    return request;
  }
}
