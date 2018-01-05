package io.lovelacetech.server.util;

public class Messages {
  public static final String DEFAULT = "an unknown error occurred";
  public static final String SUCCESS = "success";

  /*
   * Company response messages
   */
  public static final String NO_COMPANIES_FOUND = "no companies found";
  public static String NO_COMPANY_FOUND_BY_NAME(String name) {
    return "no company found with name: " + name;
  }
  public static String NO_COMPANY_FOUND_BY_PHONE_NUMBER(String phoneNumber) {
    return "no company found with phone number: " + phoneNumber;
  }

  /*
   * User response messages
   */
  public static final String NO_USERS_FOUND = "no users found";
  public static String NO_USER_FOUND_BY_EMAIL(String email) {
    return "no user found with email: " + email;
  }
  public static String NO_USER_FOUND_BY_EMAIL_PASSWORD(String email) {
    return "no user found email and password: " + email;
  }

  /*
   * Authentication messages
   */
  public static final String LOGIN_INVALID_BODY = "Please fill in usernameOrEmail and password.";
  public static final String LOGIN_INVALID_CREDENTIALS = "Invalid login. Please check your email/username and password";
}
