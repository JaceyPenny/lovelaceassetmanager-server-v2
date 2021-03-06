package io.lovelacetech.server.util;

public class Messages {
  public static final String API_WELCOME = "Welcome to the LovelaceAssetManager API. We will soon be including documentation here, but for now, authenticate yourself at \"/api/authenticate\"";

  public static final String DEFAULT = "An unknown error occurred";
  public static final String SUCCESS = "success";
  public static final String NOT_FOUND = "Requested resource not found";
  public static final String ACCESS_DENIED = "Access denied";
  public static final String INVALID_BODY = "Invalid body. Please fill in all required fields";
  public static final String CONFLICT = "There was a conflict with another record in the database";
  public static final String CANNOT_MODIFY = "You have attempted to modify a non-modifiable field";
  public static final String USER_DOES_NOT_BELONG_TO_COMPANY = "The calling user does not belong to a company";

  /*
   * Company response messages
   */
  public static final String NO_COMPANIES_FOUND = "No companies found";
  public static final String COMPANY_CONFLICTING_NAME = "Another company already has this name";

  public static String NO_COMPANY_FOUND_BY_NAME(String name) {
    return "No company found with name: " + name;
  }

  public static String NO_COMPANY_FOUND_BY_PHONE_NUMBER(String phoneNumber) {
    return "No company found with phone number: " + phoneNumber;
  }

  /*
   * User response messages
   */
  public static final String USER_ALREADY_HAS_LOCATION = "This user already has permissions for this location";
  public static final String NO_USERS_FOUND = "No users found";
  public static final String NO_USER_FOUND_BY_ID = "No users found with this id";

  public static String NO_USER_FOUND_BY_EMAIL(String email) {
    return "No user found with email: " + email;
  }

  public static String NO_USER_FOUND_BY_EMAIL_PASSWORD(String email) {
    return "No user found email and password: " + email;
  }

  /*
   * Location response messages
   */
  public static final String NO_LOCATIONS_FOUND = "No locations found";
  public static final String LOCATION_CONFLICTING_NAME = "Another location at this company already has this name";
  public static final String NO_LOCATION_FOUND_WITH_ID = "No location was found with this id";
  public static final String LOCATION_BAD_ID = "companyId does not reference a company";
  public static final String LOCATION_MUST_REMOVE_DEVICES = "You must remove all the devices at this location";
  public static final String LOCATION_MUST_REMOVE_ASSETS = "You must remove all the assets at this location";

  /*
   * Device response messages
   */
  public static final String DEVICE_CONFLICTING_DEVICE_CODE = "Another device already has this device code";
  public static final String DEVICE_ALREADY_ACTIVATED = "This device has already been activated";
  public static final String DEVICE_NO_DEVICE_CODE = "No device was found with this device code";
  public static final String DEVICE_CANNOT_MODIFY_DEVICE_CODE = "Device codes cannot be updated";
  public static final String DEVICE_CANNOT_SHARE_NAME = "There is already another device with that name at this location";
  public static final String DEVICE_BAD_ID = "locationId does not reference a location";

  /*
   * Asset response messages
   */
  public static final String ASSET_CONFLICTING_RFID = "Another asset already has this RFID tag";
  public static final String ASSET_BAD_ID = "One or more of the IDs you've specified do not reference proper locations or devices. check your input";
  public static final String ASSET_CANNOT_CREATE_NEW_ASSET_TYPE = "Only admins can specify new asset types";

  /*
   * Authentication messages
   */
  public static final String LOGIN_INVALID_BODY = "Please fill in usernameOrEmail and password.";
  public static final String LOGIN_INVALID_CREDENTIALS = "Invalid login. Please check your email/username and password";

  /*
   * Registration messages
   */
  public static final String REGISTRATION_EMAIL_ALREADY_EXISTS = "Another user is using this email";
  public static final String REGISTRATION_USERNAME_ALREADY_EXISTS = "Another user is using this username";
  public static final String REGISTRATION_PASSWORD_INVALID = "The password must meet the minimum strength requirements";
  public static final String REGISTRATION_EMAIL_INVALID = "The email supplied is not in the valid format";
}
