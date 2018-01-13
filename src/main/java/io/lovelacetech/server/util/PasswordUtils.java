package io.lovelacetech.server.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

public class PasswordUtils {
  /**
   * Requires the password to be 8 characters long, contain one uppercase letter,
   * one lowercase letter, and one digit
   */
  private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

  public static String encode(String password) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(password);
  }

  public static boolean isValidPassword(String password) {
    Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
    return passwordPattern.matcher(password).matches();
  }
}
