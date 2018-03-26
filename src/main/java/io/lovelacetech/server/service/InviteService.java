package io.lovelacetech.server.service;

import com.sendgrid.Mail;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.Invite;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiInvite;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.repository.InviteRepository;
import io.lovelacetech.server.util.RegistrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class InviteService {

  private final EmailManager emailManager;
  private final CodeGenerator codeGenerator;

  private final CompanyRepository companyRepository;
  private final InviteRepository inviteRepository;

  @Autowired
  public InviteService(
      EmailManager emailManager,
      CodeGenerator codeGenerator,
      CompanyRepository companyRepository,
      InviteRepository inviteRepository) {
    this.emailManager = emailManager;
    this.codeGenerator = codeGenerator;
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
    ApiCompany company = companyRepository.findOne(newInvite.getUser().getCompanyId()).toApi();
    emailManager.sendInvite(invite, company);

    return newInvite.toApi();
  }

  private Invite createInvite(ApiUser user, String email) {
    Invite newInvite = new Invite();
    newInvite.setUser(user.toDatabase());
    newInvite.setCode(codeGenerator.generateInviteCode());
    newInvite.setEmail(email);

    return newInvite;
  }
}
