-- CREATE EXTENSIONS
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- COMPANY
CREATE TABLE lovelace.company (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT company_pkey PRIMARY KEY,
  phone_number varchar(12) DEFAULT '000000000000'::character varying,
  name varchar);

CREATE UNIQUE INDEX company_id_uindex ON lovelace.company (id);


-- USERS
CREATE TABLE lovelace.users (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT user_pkey PRIMARY KEY,
  email varchar NOT NULL,
  username varchar(20) NOT NULL,
  password varchar NOT NULL,
  access_level smallint DEFAULT 0 NOT NULL,
  company_id uuid CONSTRAINT users_company_id_fk REFERENCES lovelace.company,
  first_name varchar,
  last_name varchar);

CREATE UNIQUE INDEX users_id_uindex ON lovelace.users (id);
CREATE UNIQUE INDEX users_email_uindex ON lovelace.users (email);
CREATE UNIQUE INDEX users_username_uindex ON lovelace.users (username);


-- LOCATION
CREATE TABLE IF NOT EXISTS lovelace.location (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT location_pkey PRIMARY KEY,
  company_id uuid NOT NULL CONSTRAINT location_company_id_fk REFERENCES lovelace.company,
  city varchar NOT NULL,
  state varchar(2) NOT NULL,
  name varchar NOT NULL);

CREATE UNIQUE INDEX IF NOT EXISTS location_id_uindex ON lovelace.location (id);


-- DEVICE
CREATE TABLE lovelace.device (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT device_pkey PRIMARY KEY,
  device_code varchar DEFAULT '00000000'::character varying NOT NULL,
  location_id uuid CONSTRAINT device_location_id_fk REFERENCES lovelace.location,
  name varchar NOT NULL);

CREATE UNIQUE INDEX device_id_uindex ON lovelace.device (id);
CREATE UNIQUE INDEX device_device_code_uindex ON lovelace.device (device_code);


-- ASSET_TYPE
CREATE TABLE IF NOT EXISTS lovelace.asset_type (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT asset_type_pkey PRIMARY KEY,
  company_id uuid NOT NULL CONSTRAINT "asset_type_lovelace.company_id_fk" REFERENCES lovelace.company,
  type varchar NOT NULL);

CREATE UNIQUE INDEX IF NOT EXISTS asset_type_id_uindex ON lovelace.asset_type (id);


-- ASSET
CREATE TABLE lovelace.asset (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT asset_pkey PRIMARY KEY,
  rfid varchar NOT NULL,
  serial varchar NOT NULL,
  status varchar, home_id uuid NOT NULL CONSTRAINT asset_home_id_fk REFERENCES lovelace.device,
  location_id uuid CONSTRAINT asset_location_id_fk REFERENCES lovelace.location,
  device_id uuid CONSTRAINT asset_device_id_fk REFERENCES lovelace.device,
  name varchar,
  asset_type_id uuid CONSTRAINT asset_asset_type_id_fk REFERENCES lovelace.asset_type);

CREATE UNIQUE INDEX asset_id_uindex ON lovelace.asset (id);
CREATE UNIQUE INDEX asset_rfid_uindex ON lovelace.asset (rfid);


-- LOG
CREATE TABLE lovelace.log (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT log_pkey PRIMARY KEY,
  type smallint NOT NULL,
  timestamp TIMESTAMP DEFAULT now() NOT NULL,
  user_id uuid CONSTRAINT log_users_id_fk REFERENCES lovelace.users,
  object_id uuid NOT NULL,
  old_data varchar,
  new_data varchar);

CREATE UNIQUE INDEX log_id_uindex ON lovelace.log (id);


-- NOTIFICATION
CREATE TABLE lovelace.notification (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT notification_pkey PRIMARY KEY,
  notification_type varchar DEFAULT 'email'::character varying NOT NULL,
  time TIME DEFAULT '00:00:00'::TIME WITHOUT TIME ZONE NOT NULL,
  user_id uuid NOT NULL CONSTRAINT notification_user_id_fk REFERENCES lovelace.users,
  active boolean DEFAULT TRUE);

CREATE UNIQUE INDEX notification_id_uindex ON lovelace.notification (id);


-- USERS_LOCATIONS
CREATE TABLE IF NOT EXISTS lovelace.users_locations (
  user_id uuid NOT NULL CONSTRAINT users_locations_user_id_fk REFERENCES lovelace.users,
  location_id uuid NOT NULL CONSTRAINT users_locations_location_id_fk REFERENCES lovelace.location,

CONSTRAINT users_locations_user_id_location_id_pk PRIMARY KEY (user_id, location_id));


-- NOTIFICATION_ITEMS
CREATE TABLE IF NOT EXISTS lovelace.notification_items (
  notification_id uuid NOT NULL CONSTRAINT notification_items_notification_id_fk REFERENCES lovelace.notification,
  location_id uuid CONSTRAINT notification_items_location_id_fk REFERENCES lovelace.location,
  device_id uuid CONSTRAINT notification_items_device_id_fk REFERENCES lovelace.device);


-- INVITE
CREATE TABLE IF NOT EXISTS lovelace.invite (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT invite_pkey PRIMARY KEY,
  user_id uuid NOT NULL CONSTRAINT invite_users_id_fk REFERENCES lovelace.users,
  code varchar NOT NULL,
  email varchar NOT NULL);

CREATE UNIQUE INDEX IF NOT EXISTS invite_id_uindex ON lovelace.invite (id);
CREATE UNIQUE INDEX IF NOT EXISTS invite_code_uindex ON lovelace.invite (code);
