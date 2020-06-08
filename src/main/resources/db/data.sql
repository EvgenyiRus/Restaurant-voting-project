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

INSERT INTO DISHES (DESCRIPTION, PRICE)
VALUES ('Суп борщ', 255),               --100002
       ('Суп харчо', 275),              --100003
       ('Жаркое из баранины', 500.65),  --100004
       ('Пюре с тефтелями', 325.5),     --100005
       ('Свинина по-французски', 500),  --100006
       ('Десерт мороженное', 100),      --100007
       ('Яблочный пирог', 300),         --100008
       ('Трюфель', 500),                --100009
       ('Апельсиновый сок', 150.34),    --100010
       ('Чай', 99),                     --100011
       ('Кофе каппучино', 129),         --100012
       ('Хлеб', 150.34);                --100013

INSERT into RESTAURANTS(name)
values ('Каудаль'),                     --100014
       ('В гостях'),                    --100015
       ('У Марифа'),                    --100016
       ('Солнечный');                   --100017

INSERT into MENU_ITEMS(date, RESTAURANT_ID, dish_id)
VALUES(now,100014,100002),
      (now,100014,100004),
      (now,100014,100007),
      (now,100014,100010),
      (now,100015,100003),
      (now,100015,100005),
      (now,100015,100008),
      (now,100015,100011),
      (now,100016,100002),
      (now,100016,100006),
      (now,100016,100009),
      (now,100016,100012),
      (now,100016,100013),
      (now,100017,100003),
      (now,100017,100005),
      (now,100017,100011),
      (now,100017,100013),
      (date_add(now,-1),100014,100002),
      (date_add(now,-1),100014,100006),
      (date_add(now,-1),100014,100009),
      (date_add(now,-1),100014,100013),
      (date_add(now,-1),100015,100003),
      (date_add(now,-1),100015,100004),
      (date_add(now,-1),100015,100008),
      (date_add(now,-1),100015,100012),
      (date_add(now,-1),100016,100003),
      (date_add(now,-1),100016,100006),
      (date_add(now,-1),100016,100008),
      (date_add(now,-1),100016,100012),
      (date_add(now,-1),100017,100003),
      (date_add(now,-1),100017,100005),
      (date_add(now,-1),100017,100010),
      (date_add(now,-1),100017,100013);

INSERT INTO users (name, email, password) VALUES
('User', 'quest@yandex.ru', '{noop}quest'),        --100018
('User', 'vasya@gmail.com', '{noop}vasya'),        --100019
('User', 'Gosha@gmail.com', '{noop}gosha'),         --100020
('User', 'trol@gmail.com', '{noop}trol');           --100021

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