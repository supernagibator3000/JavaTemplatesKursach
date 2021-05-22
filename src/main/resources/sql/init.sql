create table if not exists roles_table(
    id serial primary key not null,
    name text not null
);

create table if not exists users_table(
    id serial primary key not null,
    username text not null unique,
    password text not null,
    email text not null unique
);

create table if not exists items_table(
    id serial primary key not null,
    name text not null,
    price float8 not null,
    description text
);

create table if not exists orders_table(
    id serial primary key not null,
    is_ordered boolean not null default false,
    price float8,
    user_id serial
);

create table if not exists users_table_roles(
    users_id serial not null,
    roles_id serial not null
);

create table if not exists orders_table_items(
    orders_id serial not null,
    items_id serial not null
);

-- Внести роли в таблицу при первом запуске
INSERT INTO roles_table (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles_table (id, name) VALUES (2, 'ROLE_ADMIN');

-- Сделать админа
insert into users_table (id, username, password, email) values (0, 'admin', 'admin','javatemplates03@gmail.com');
INSERT INTO users_table_roles (users_id, roles_id) VALUES (0, 1);
INSERT INTO users_table_roles (users_id, roles_id) VALUES (0, 2);

create extension if not exists pgcrypto;
update users_table set password = crypt(password, gen_salt('bf', 10));