package io.lovelacetech.server.service;

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

  public String build(ApiUser user, ApiNotification notification) {
    Context context = new Context();
    context.setVariable("notification", notification);
    context.setVariable("user", user);
    return templateEngine.process("notification_template", context);
  }
}
