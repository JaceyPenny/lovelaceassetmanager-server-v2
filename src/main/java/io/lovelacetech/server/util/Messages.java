package io.lovelacetech.server.util;

public class Messages {
  public static final String API_WELCOME = "Welcome to the LovelaceAssetManager API. We will soon be including documentation here, but for now, authenticate yourself at \"/api/authenticate\"";

  public static final String DEFAULT = "an unknown error occurred";
  public static final String SUCCESS = "success";
  public static final String NOT_FOUND = "requested resource not found";
  public static final String ACCESS_DENIED = "access denied";
  public static final String INVALID_BODY = "invalid body. please fill in all required fields";

  /*
   * Company response messages
   */
  public static final String NO_COMPANIES_FOUND = "no companies found";
  public static final String COMPANY_CONFLICTING_NAME = "another company already has this name";
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
  public static final String NO_USER_FOUND_BY_ID = "no users found with this id";
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

  /*
   * Registration messages
   */
  public static final String REGISTRATION_EMAIL_ALREADY_EXISTS = "another user is using this email";
  public static final String REGISTRATION_USERNAME_ALREADY_EXISTS = "another user is using this username";
  public static final String REGISTRATION_PASSWORD_INVALID = "the password must meet the minimum strength requirements";
}
