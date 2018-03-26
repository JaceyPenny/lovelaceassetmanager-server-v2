package io.lovelacetech.server.service;

import io.lovelacetech.server.model.PasswordReset;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.repository.PasswordResetRepository;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.RegistrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

  private final EmailManager emailManager;

  private final PasswordResetRepository passwordResetRepository;
  private final UserRepository userRepository;
  private final CodeGenerator codeGenerator;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public PasswordResetService(
      EmailManager emailManager,
      PasswordResetRepository passwordResetRepository,
      UserRepository userRepository,
      CodeGenerator codeGenerator,
      PasswordEncoder passwordEncoder) {
    this.emailManager = emailManager;
    this.passwordResetRepository = passwordResetRepository;
    this.userRepository = userRepository;
    this.codeGenerator = codeGenerator;
    this.passwordEncoder = passwordEncoder;
  }

  public PasswordReset requestReset(String email) {
    if (!RegistrationUtils.isValidEmail(email)) {
      return null;
    }

    User lookupUser = userRepository.findByEmail(email);
    if (lookupUser == null) {
      return null;
    }

    PasswordReset existingPasswordReset = passwordResetRepository.findByUser(lookupUser);
    if (existingPasswordReset != null) {

      if (existingPasswordReset.getExpiration().isBefore(LocalDateTime.now())) {
        passwordResetRepository.delete(existingPasswordReset);
      } else {
        return null;
      }
    }

    PasswordReset newPasswordReset = new PasswordReset();
    newPasswordReset.setUser(lookupUser);
    newPasswordReset.setCode(codeGenerator.generatePasswordResetCode());
    newPasswordReset.setExpiration(LocalDateTime.now().plusDays(1));

    emailManager.sendPasswordReset(newPasswordReset.toApi());

    return passwordResetRepository.save(newPasswordReset);
  }

  public boolean resetPassword(String resetCode, String newPassword) {
    if (!RegistrationUtils.isValidPassword(newPassword)) {
      return false;
    }

    PasswordReset passwordReset = passwordResetRepository.findByCode(resetCode);
    if (passwordReset == null) {
      return false;
    }

    User user = passwordReset.getUser();

    String hashedNewPassword = passwordEncoder.encode(newPassword);
    user.setPassword(hashedNewPassword);
    userRepository.save(user);

    passwordResetRepository.delete(passwordReset);

    return true;
  }
}
