CREATE TABLE IF NOT EXISTS migrations (
  key text CONSTRAINT pkey PRIMARY KEY)
----
-- src: https://gist.github.com/pesterhazy/9f7c0a7a9edd002759779c1732e0ac43
CREATE OR REPLACE FUNCTION idempotent (migration_name text, code text)
  RETURNS void
  AS $$
BEGIN
  IF EXISTS (
    SELECT
      key
    FROM
      migrations
    WHERE
      key = migration_name) THEN
  RAISE NOTICE 'Migration already applied: %', migration_name;
ELSE
  RAISE NOTICE 'Running migration: %', migration_name;
  EXECUTE code;
  INSERT INTO migrations (key)
    VALUES (migration_name);
END IF;
END;
$$
LANGUAGE plpgsql
STRICT;

----
CREATE TABLE IF NOT EXISTS my_test (
  id int
);

----
INSERT INTO my_test (id)
  VALUES (42);

----
CREATE TABLE IF NOT EXISTS recipes (
  id serial PRIMARY KEY,
  name text
);

----
CREATE TABLE IF NOT EXISTS ingredients (
  id serial PRIMARY KEY,
  name text
);

----
CREATE TABLE IF NOT EXISTS recipe_ingredients (
  id serial PRIMARY KEY,
  ingredient_id int REFERENCES ingredients (id),
  recipe_id int REFERENCES recipes (id),
  qty decimal
);

