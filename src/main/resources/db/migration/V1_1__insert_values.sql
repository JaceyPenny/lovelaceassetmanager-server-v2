DO $$
  DECLARE tyson_id lovelace.company.id%TYPE;

  DECLARE super_user_id lovelace.users.id%TYPE;
  DECLARE admin_user_id lovelace.users.id%TYPE;
  DECLARE user_user_id lovelace.users.id%TYPE;

  DECLARE fayetteville_location_id lovelace.location.id%TYPE;
  DECLARE bentonville_location_id lovelace.location.id%TYPE;

  DECLARE device_1_id lovelace.device.id%TYPE;
  DECLARE device_2_id lovelace.device.id%TYPE;
  DECLARE device_3_id lovelace.device.id%TYPE;
  DECLARE device_4_id lovelace.device.id%TYPE;

  DECLARE default_asset_type_id lovelace.asset_type.id%TYPE;
  DECLARE scanner_asset_type_id lovelace.asset_type.id%TYPE;

  DECLARE email_notification_1_id lovelace.notification.id%TYPE;
  DECLARE email_text_notification_2_id lovelace.notification.id%TYPE;
  DECLARE text_notification_3_id lovelace.notification.id%TYPE;
BEGIN
  -- COMPANIES
  INSERT INTO lovelace.company (name, phone_number)
    VALUES ('Tyson', '5016268081')
    RETURNING id INTO tyson_id;

  -- USERS
  INSERT INTO lovelace.users (access_level, company_id, email, first_name, last_name, password, username)
    VALUES (3, tyson_id, 'jace@lovelacetech.io', 'Jace', 'McPherson', '$2a$08$1oz0N5rdU7R1Q.gogB9sAe98GmVtvj0qaVN50X5yD63nbteqwqS0y', 'jacelovelace')
    RETURNING id INTO super_user_id;

  INSERT INTO lovelace.users (access_level, company_id, email, first_name, last_name, password, username)
    VALUES (2, tyson_id, 'jaceyjace1997@gmail.com', 'Jace', 'McPherson', '$2a$08$kYwR7vEGJg.2BtYTM6KR.OEsRr8AIVOv0wyhaREu5LMrfohFBjPMm', 'jacegmail')
    RETURNING id INTO admin_user_id;

  INSERT INTO lovelace.users (access_level, company_id, email, first_name, last_name, password, username)
    VALUES (1, tyson_id, 'jace-mcpherson@att.net', 'Jace', 'McPherson', '$2a$08$fIY5yRBM4AI6f5AT478V2ePDFkGqpDkAJW3hGZJthXzP3vmjMGnA2', 'jaceatt')
    RETURNING id INTO user_user_id;

  -- LOCATIONS
  INSERT INTO lovelace.location (city, company_id, name, state)
    VALUES ('Fayetteville', tyson_id, 'Fayetteville Plant', 'AR')
    RETURNING id INTO fayetteville_location_id;

  INSERT INTO lovelace.location (city, company_id, name, state)
    VALUES ('Bentonville', tyson_id, 'Bentonville Plant', 'AR')
    RETURNING id INTO bentonville_location_id;


  -- DEVICES
  INSERT INTO lovelace.device (device_code, location_id, name)
    VALUES ('3AE0E7FA2B', fayetteville_location_id, 'Device 1')
    RETURNING id into device_1_id;

  INSERT INTO lovelace.device (device_code, location_id, name)
    VALUES ('AB5030FB82', fayetteville_location_id, 'Device 2')
    RETURNING id into device_2_id;

  INSERT INTO lovelace.device (device_code, location_id, name)
    VALUES ('93AA6EB5A7', bentonville_location_id, 'Device 3')
    RETURNING id into device_3_id;

  INSERT INTO lovelace.device (device_code, location_id, name)
    VALUES ('F4B0A09A70', bentonville_location_id, 'Device 4')
    RETURNING id into device_4_id;


  -- ASSET_TYPES
  INSERT INTO lovelace.asset_type (company_id, type)
    VALUES (tyson_id, 'Asset')
    RETURNING id INTO default_asset_type_id;

  INSERT INTO lovelace.asset_type (company_id, type)
    VALUES (tyson_id, 'Scanner')
    RETURNING id INTO scanner_asset_type_id;

  -- ASSETS
  INSERT INTO lovelace.asset (device_id, home_id, location_id, name, rfid, serial, status, asset_type_id)
    VALUES
      (device_1_id, device_1_id, NULL, 'Scanner 1', 'AD42030441CD6D775E0000CB', '857502842431', 'available', scanner_asset_type_id),
      (device_1_id, device_1_id, NULL, 'Scanner 2', 'EAB35BE3B672B3DCC99F2F6A', '381951263294', 'available', scanner_asset_type_id),
      (device_2_id, device_2_id, NULL, 'Scanner 3', '2713F5400FBC63E221E65C22', '759368025612', 'available', scanner_asset_type_id),
      (device_2_id, device_2_id, NULL, 'Asset 4', '094EA36030CF4E409A43393C', '759647774934', 'available', default_asset_type_id),
      (NULL, device_2_id, fayetteville_location_id, 'Scanner 5', '57B90DB7E29109F046DA7D36', '388759132819', 'available', scanner_asset_type_id),
      (device_3_id, device_3_id, NULL, 'Scanner 6', 'E847073AFF708EBE22686639', '581322516325', 'available', scanner_asset_type_id),
      (NULL, device_3_id, bentonville_location_id, 'Scanner 7', 'EC9615C9216E4A5FB55DE058', '931834061295', 'available', scanner_asset_type_id),
      (NULL, device_3_id, bentonville_location_id, 'Asset 8', 'EFD26427CD66A933F5FAFDA6', '046036327044', 'available', default_asset_type_id),
      (device_4_id, device_4_id, NULL, 'Asset 9', 'BE56EC9D863677650D510B56', '177970240834', 'available', default_asset_type_id);


  -- USERS_LOCATIONS
  INSERT INTO lovelace.users_locations (user_id, location_id)
    VALUES (user_user_id, fayetteville_location_id);

  -- NOTIFICATION
  INSERT INTO lovelace.notification (active, notification_type, time, user_id)
    VALUES (true, 'email', '12:00:00', admin_user_id)
    RETURNING id INTO email_notification_1_id;

  INSERT INTO lovelace.notification (active, notification_type, time, user_id)
    VALUES (true, 'email_text', '13:00:00', user_user_id)
    RETURNING id INTO email_text_notification_2_id;

  INSERT INTO lovelace.notification (active, notification_type, time, user_id)
    VALUES (true, 'text', '14:00:00', super_user_id)
    RETURNING id INTO text_notification_3_id;


  -- NOTIFICATION_ITEMS
  INSERT INTO lovelace.notification_items (notification_id, location_id, device_id)
    VALUES
      (email_notification_1_id, NULL, device_2_id),
      (email_notification_1_id, NULL, device_1_id),
      (email_text_notification_2_id, fayetteville_location_id, NULL),
      (text_notification_3_id, bentonville_location_id, NULL),
      (text_notification_3_id, NULL, device_4_id);
END;$$;
