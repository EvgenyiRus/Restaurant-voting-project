delete from users;
delete from dishes;
delete from RESTAURANTS;
delete from VOTES;
delete from MENU_ITEMS;

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;
ALTER SEQUENCE vote_seq RESTART WITH 1;
ALTER SEQUENCE menu_items_seq RESTART WITH 1;

INSERT INTO users (name, email, password) VALUES
('User', 'user@yandex.ru', '{noop}password'),
('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id) VALUES
('USER', 100000),
('ADMIN', 100001),
('USER', 100001);

INSERT INTO DISHES (DESCRIPTION)
VALUES ('Суп борщ'),               --100002
       ('Суп харчо'),              --100003
       ('Жаркое из баранины'),     --100004
       ('Пюре с тефтелями'),       --100005
       ('Свинина по-французски'),  --100006
       ('Десерт мороженное'),      --100007
       ('Яблочный пирог'),         --100008
       ('Трюфель'),                --100009
       ('Апельсиновый сок'),       --100010
       ('Чай'),                    --100011
       ('Кофе каппучино'),         --100012
       ('Хлеб');                   --100013

INSERT into RESTAURANTS(name)
values ('Каудаль'),                --100014
       ('В гостях'),               --100015
       ('У Марифа'),               --100016
       ('Солнечный');              --100017

INSERT into MENU_ITEMS(date, RESTAURANT_ID, dish_id, price)
VALUES(now,100014,100002, 255),
      (now,100014,100004, 500.65),
      (now,100014,100007, 100),
      (now,100014,100010, 150.34),
      (now,100015,100003, 275),
      (now,100015,100005, 325.5),
      (now,100015,100008, 300),
      (now,100015,100011, 99),
      (now,100016,100002, 267),
      (now,100016,100006, 500),
      (now,100016,100009, 500),
      (now,100016,100012, 129),
      (now,100016,100013, 10),
      (now,100017,100003, 285),
      (now,100017,100005, 325.5),
      (now,100017,100011, 95),
      (now,100017,100013, 15),
      (date_add(now,-1),100014,100002, 255.5),
      (date_add(now,-1),100014,100006, 501),
      (date_add(now,-1),100014,100009, 503),
      (date_add(now,-1),100014,100013, 13),
      (date_add(now,-1),100015,100003, 275),
      (date_add(now,-1),100015,100004, 505.65),
      (date_add(now,-1),100015,100008, 301.7),
      (date_add(now,-1),100015,100012, 145),
      (date_add(now,-1),100016,100003, 277),
      (date_add(now,-1),100016,100006, 505),
      (date_add(now,-1),100016,100008, 305),
      (date_add(now,-1),100016,100012, 134),
      (date_add(now,-1),100017,100003, 285),
      (date_add(now,-1),100017,100005, 324),
      (date_add(now,-1),100017,100010, 168),
      (date_add(now,-1),100017,100013, 13);

INSERT INTO users (name, email, password) VALUES
('User', 'quest@yandex.ru', '{noop}quest'),        --100018
('User', 'vasya@gmail.com', '{noop}vasya'),        --100019
('User', 'Gosha@gmail.com', '{noop}gosha'),        --100020
('User', 'trol@gmail.com', '{noop}trol');          --100021

INSERT INTO user_roles (role, user_id) VALUES
('USER', 100018),
('USER', 100019),
('USER', 100020),
('USER', 100021);

INSERT INTO VOTES (date,RESTAURANT_ID,USER_ID) VALUES
        (now,100014,100000),
        (now,100014,100018),
        (now,100014,100019),
        (now,100014,100020),
        (now,100015,100021),
        (date_add(now,-1),100014,100000),
        (date_add(now,-1),100015,100018),
        (date_add(now,-1),100015,100019),
        (date_add(now,-1),100016,100020),
        (date_add(now,-1),100016,100021),
        (date_add(now,-1),100017,100000),
        (date_add(now,-2),100017,100018),
        (date_add(now,-2),100014,100019),
        (date_add(now,-2),100016,100020),
        (date_add(now,-2),100016,100021);