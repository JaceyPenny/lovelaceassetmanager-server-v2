package io.lovelacetech.server.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGenerator {

  private String codeCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private Random random = new Random();

  public String generateInviteCode() {
    return generateAlphanumericWithLength(10);
  }

  public String generatePasswordResetCode() {
    return generateAlphanumericWithLength(24);
  }

  private String generateAlphanumericWithLength(int length) {
    StringBuilder inviteCodeBuilder = new StringBuilder();

    for (int i = 0; i < length; i++) {
      inviteCodeBuilder.append(randomAlphanumeric());
    }

    return inviteCodeBuilder.toString();
  }

  private char randomAlphanumeric() {
    return codeCharacters.charAt(random.nextInt(codeCharacters.length()));
  }
}
