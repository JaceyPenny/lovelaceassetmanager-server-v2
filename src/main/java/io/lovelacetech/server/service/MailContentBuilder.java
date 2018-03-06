package io.lovelacetech.server.service;

import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {
  private TemplateEngine templateEngine;

  @Autowired
  public MailContentBuilder(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public String buildNotification(ApiUser user, ApiNotification notification) {
    Context context = new Context();
    context.setVariable("notification", notification);
    context.setVariable("user", user);
    return templateEngine.process("notification_template", context);
  }

  public String buildInvite(String companyName, ApiInvite invite) {
    String sponsorName = invite.getUser().getFirstName() + " " + invite.getUser().getLastName();

    Context context = new Context();
    context.setVariable("companyName", companyName);
    context.setVariable("sponsorName", sponsorName);
    context.setVariable("invite", invite);
    return templateEngine.process("email_invite_template", context);
  }
}
