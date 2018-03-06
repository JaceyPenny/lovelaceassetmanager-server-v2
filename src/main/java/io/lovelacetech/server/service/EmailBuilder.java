package io.lovelacetech.server.service;

import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailBuilder {
  protected Mail buildMail(
      String fromEmail,
      String fromName,
      String subject,
      String toEmail,
      String html) {
    Email from = new Email(fromEmail);
    from.setName(fromName);
    Email to = new Email(toEmail);
    Content content = new Content("text/html", html);
    return new Mail(from, subject, to, content);
  }

  protected Request buildRequest(Mail mail) throws IOException {
    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    return request;
  }
}
