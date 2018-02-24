package io.lovelacetech.server.service;

import com.sendgrid.*;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.LoaderUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationService {

  private final SendGrid sendGrid;
  private final MailContentBuilder mailContentBuilder;

  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  public NotificationService(
      SendGrid sendGrid,
      MailContentBuilder mailContentBuilder,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    this.sendGrid = sendGrid;
    this.mailContentBuilder = mailContentBuilder;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
  }

  public void sendNotification(ApiUser user, ApiNotification notification) {
    LoaderUtils.populateNotification(
        notification, deviceRepository, assetRepository, logRepository);

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
    String subject = "Your Daily Inventory Report";
    Email to = new Email("jace@lovelacetech.io");

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
