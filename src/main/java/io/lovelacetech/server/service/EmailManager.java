package io.lovelacetech.server.service;

import com.sendgrid.Mail;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.model.api.model.ApiPasswordReset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailManager {

  private final MailContentBuilder mailContentBuilder;
  private final EmailBuilder emailBuilder;
  private final SendGrid sendGrid;

  @Autowired
  public EmailManager(
      MailContentBuilder mailContentBuilder,
      EmailBuilder emailBuilder,
      SendGrid sendGrid) {
    this.mailContentBuilder = mailContentBuilder;
    this.emailBuilder = emailBuilder;
    this.sendGrid = sendGrid;
  }

  public void sendInvite(ApiInvite invite, ApiCompany company) {
    String html = mailContentBuilder.buildInvite(company.getName(), invite);

    Mail mail = emailBuilder.buildMail(
        "invite@lovelacetech.io",
        "Lovelace Technologies Invite",
        "You've Been Invited to Join " + company.getName(),
        invite.getEmail(),
        html);

    try {
      Request request = emailBuilder.buildRequest(mail);
      sendGrid.api(request);
    } catch (IOException e) {
      // do nothing
    }
  }

  public void sendPasswordReset(ApiPasswordReset passwordReset) {
    String html = mailContentBuilder.buildPasswordReset(passwordReset);

    Mail mail = emailBuilder.buildMail(
        "reset@lovelacetech.io",
        "Lovelace Technologies Password Reset",
        "Reset Your Lovelace Technologies Password",
        passwordReset.getUser().getEmail(),
        html);

    try {
      Request request = emailBuilder.buildRequest(mail);
      sendGrid.api(request);
    } catch (IOException e) {
      // do nothing
    }
  }
}
