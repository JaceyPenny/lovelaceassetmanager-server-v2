package io.lovelacetech.server.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.regex.Pattern;

public class RegistrationUtils {
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

  public static boolean isValidEmail(String email) {
    try {
      InternetAddress emailAddress = new InternetAddress(email);
      emailAddress.validate();
      return true;
    } catch (AddressException addressException) {
      return false;
    }
  }
}
