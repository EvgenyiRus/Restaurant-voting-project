DROP TABLE votes IF EXISTS;
DROP TABLE menu_items IF EXISTS;
DROP TABLE user_roles IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE dishes IF EXISTS;
DROP TABLE restaurants IF EXISTS;

DROP SEQUENCE global_seq IF EXISTS;
DROP SEQUENCE vote_seq IF EXISTS;
DROP SEQUENCE menu_items_seq IF EXISTS;

-- Create indexes
CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;
CREATE SEQUENCE VOTE_SEQ AS BIGINT START WITH 1;
CREATE SEQUENCE MENU_ITEMS_SEQ AS BIGINT START WITH 1;

-- Create tables
------------------------------------------------------------------------------------------
CREATE TABLE users
(
    id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name             VARCHAR(255)            NOT NULL,
    email            VARCHAR(255)            NOT NULL,
    password         VARCHAR(255)            NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);
------------------------------------------------------------------------------------------
CREATE TABLE user_roles
(
    user_id         INTEGER NOT NULL,
    role            VARCHAR(255),
    CONSTRAINT user_roles_unique_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);
------------------------------------------------------------------------------------------
CREATE TABLE restaurants
(
    id              INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name            VARCHAR(255) NOT NULL UNIQUE
);
------------------------------------------------------------------------------------------
CREATE TABLE dishes
(
    id              INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    description     VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX dishes_unique_description_idx
    ON dishes (description);
------------------------------------------------------------------------------------------
CREATE TABLE menu_items(
    id              INTEGER GENERATED BY DEFAULT AS SEQUENCE MENU_ITEMS_SEQ PRIMARY KEY,
    date            DATE default now,
    restaurant_id   INTEGER NOT NULL,
    dish_id         INTEGER NOT NULL,
    price           double default 0 NOT NULL,
    FOREIGN KEY(dish_id) REFERENCES dishes (id) ON DELETE CASCADE,
    FOREIGN KEY(restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT menus_items_idx UNIQUE (restaurant_id,date,dish_id)
);
CREATE INDEX menu_items_idx
    ON menu_items (dish_id,date);
------------------------------------------------------------------------------------------
CREATE TABLE votes(
    id              INTEGER GENERATED BY DEFAULT AS SEQUENCE VOTE_SEQ PRIMARY KEY,
    date            DATE default now,
    restaurant_id   INTEGER NOT NULL,
    user_id         INTEGER NOT NULL,
    CONSTRAINT vote_idx UNIQUE (user_id,restaurant_id, date),
    FOREIGN KEY(user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY(restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
CREATE INDEX votes_item_idx
    ON votes (restaurant_id,date);