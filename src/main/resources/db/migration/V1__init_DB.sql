DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000 INCREMENT 1;

CREATE TABLE users
(
    id              int8            PRIMARY KEY DEFAULT nextval('global_seq'),
    username        VARCHAR(15)             NOT NULL,
    email           VARCHAR(255)    UNIQUE  NOT NULL,
    password        VARCHAR(255)            NOT NULL,
    first_name      VARCHAR(255)            NOT NULL,
    last_name       VARCHAR(255)            NOT NULL,
    enabled         boolean DEFAULT FALSE   NOT NULL,
    profile_image   varchar(255),
    activation_code VARCHAR(255)
);
CREATE UNIQUE INDEX users_unique_username_idx ON users (username);

CREATE TABLE users_roles
(
    user_id int8    NOT NULL,
    roles   VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);