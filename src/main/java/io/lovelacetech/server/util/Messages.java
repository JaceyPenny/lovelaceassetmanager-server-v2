package io.lovelacetech.server.util;

public class Messages {
  public static final String API_WELCOME = "Welcome to the LovelaceAssetManager API. We will soon be including documentation here, but for now, authenticate yourself at \"/api/authenticate\"";

  public static final String DEFAULT = "an unknown error occurred";
  public static final String SUCCESS = "success";
  public static final String NOT_FOUND = "requested resource not found";
  public static final String ACCESS_DENIED = "access denied";
  public static final String INVALID_BODY = "invalid body. please fill in all required fields";
  public static final String CONFLICT = "there was a conflict with another record in the database";
  public static final String CANNOT_MODIFY = "you have attempted to modify a non-modifiable field";
  public static final String USER_DOES_NOT_BELONG_TO_COMPANY = "the calling user does not belong to a company";

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
  public static final String USER_ALREADY_HAS_LOCATION = "this user already has permissions for this location";
  public static final String NO_USERS_FOUND = "no users found";
  public static final String NO_USER_FOUND_BY_ID = "no users found with this id";

  public static String NO_USER_FOUND_BY_EMAIL(String email) {
    return "no user found with email: " + email;
  }

  public static String NO_USER_FOUND_BY_EMAIL_PASSWORD(String email) {
    return "no user found email and password: " + email;
  }

  /*
   * Location response messages
   */
  public static final String NO_LOCATIONS_FOUND = "no locations found";
  public static final String LOCATION_CONFLICTING_NAME = "another location at this company already has this name";
  public static final String NO_LOCATION_FOUND_WITH_ID = "no location was found with this id";
  public static final String LOCATION_BAD_ID = "companyId does not reference a company";

  /*
   * Device response messages
   */
  public static final String DEVICE_CONFLICTING_DEVICE_CODE = "another device already has this device code";
  public static final String DEVICE_ALREADY_ACTIVATED = "this device has already been activated";
  public static final String DEVICE_NO_DEVICE_CODE = "no device was found with this device code";
  public static final String DEVICE_CANNOT_MODIFY_DEVICE_CODE = "device codes cannot be updated";
  public static final String DEVICE_BAD_ID = "locationId does not reference a location";

  /*
   * Asset response messages
   */
  public static final String ASSET_CONFLICTING_RFID = "another asset already has this RFID tag";
  public static final String ASSET_BAD_ID = "one or more of the IDs you've specified do not reference proper locations or devices. check your input";
  public static final String ASSET_CANNOT_CREATE_NEW_ASSET_TYPE = "only admins can specify new asset types";

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
  public static final String REGISTRATION_EMAIL_INVALID = "the email supplied is not in the valid format";
}
