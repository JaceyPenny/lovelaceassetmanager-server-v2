package io.lovelacetech.server.service;

import com.sendgrid.*;
import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.Invite;
import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.repository.InviteRepository;
import io.lovelacetech.server.util.RegistrationUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class InviteService {
  private static String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static Random random = new Random();

  private final SendGrid sendGrid;
  private final MailContentBuilder mailContentBuilder;
  private final EmailBuilder emailBuilder;

  private final CompanyRepository companyRepository;
  private final InviteRepository inviteRepository;

  public InviteService(
      SendGrid sendGrid,
      MailContentBuilder mailContentBuilder,
      EmailBuilder emailBuilder,
      CompanyRepository companyRepository,
      InviteRepository inviteRepository) {
    this.sendGrid = sendGrid;
    this.mailContentBuilder = mailContentBuilder;
    this.emailBuilder = emailBuilder;
    this.companyRepository = companyRepository;
    this.inviteRepository = inviteRepository;
  }

  public ApiInvite inviteByEmail(ApiUser user, String email) {
    if (!RegistrationUtils.isValidEmail(email)) {
      return null;
    }

    Invite newInvite = createInvite(user, email);
    newInvite = inviteRepository.saveAndFlush(newInvite);

    ApiInvite invite = newInvite.toApi();
    sendInvite(invite);

    return newInvite.toApi();
  }

  private void sendInvite(ApiInvite invite) {
    Company company = companyRepository.findOne(invite.getUser().getCompanyId());
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

  private Invite createInvite(ApiUser user, String email) {
    Invite newInvite = new Invite();
    newInvite.setUser(user.toDatabase());
    newInvite.setCode(generateInviteCode());
    newInvite.setEmail(email);

    return newInvite;
  }

  private String generateInviteCode() {
    StringBuilder inviteCodeBuilder = new StringBuilder();

    for (int i = 0; i < 10; i++) {
      inviteCodeBuilder.append(randomAlphanumeric());
    }

    return inviteCodeBuilder.toString();
  }

  private char randomAlphanumeric() {
    return CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length()));
  }
}
