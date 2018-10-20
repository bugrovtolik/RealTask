create sequence hibernate_sequence start 2 increment 1;

create table service (
    id int8 not null,
    description varchar(2000),
    lat varchar(255),
    lng varchar(255),
    secret varchar(500),
    title varchar(255),
    user_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    avatar varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
);

alter table if exists service
    add constraint service_user_fk
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;