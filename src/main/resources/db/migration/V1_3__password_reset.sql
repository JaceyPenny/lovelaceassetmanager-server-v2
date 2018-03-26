CREATE TABLE lovelace.password_reset (
  id uuid DEFAULT uuid_generate_v4() NOT NULL CONSTRAINT password_reset_pkey PRIMARY KEY,
  user_id uuid NOT NULL CONSTRAINT password_reset_users_id_fk REFERENCES lovelace.users,
  code varchar(24) NOT NULL,
  expiration TIMESTAMP NOT NULL);

CREATE UNIQUE INDEX password_reset_id_uindex ON lovelace.password_reset (id);
CREATE UNIQUE INDEX password_reset_code_uindex ON lovelace.password_reset (code);