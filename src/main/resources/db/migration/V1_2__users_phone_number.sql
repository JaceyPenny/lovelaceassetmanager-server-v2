-- Adds a phone_number column to the users table
ALTER TABLE lovelace.users ADD COLUMN phone_number varchar;
CREATE UNIQUE INDEX IF NOT EXISTS users_phone_number_uindex ON lovelace.users (phone_number);

UPDATE lovelace.users SET phone_number = '5016268081' WHERE username = 'jacelovelace';