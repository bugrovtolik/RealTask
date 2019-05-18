set time zone 'Europe/Kiev';

create sequence hibernate_sequence start 1 increment 1;

create table task (
    id int8 not null,
    title varchar(255) not null,
    description varchar(2000) not null,
    secret varchar(500),
    exec_from timestamp not null,
    exec_to timestamp not null,
    price int4 not null,
    lat varchar(255) not null,
    lng varchar(255) not null,
    category_id int8 not null,
    user_id int8 not null,
    active boolean not null,
    paid boolean not null,
    cashless boolean not null,
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
    credit_card_number varchar(16),
    phone_number varchar(10) not null,
    credit int4 default 0,
    email varchar(255) not null,
    avatar varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

create table contract (
    id int8 not null,
    user_id int8 not null,
    task_id int8 not null,
    time timestamp not null,
    text varchar(255),
    accepted boolean,
    completed boolean,
    primary key (id)
);

create table category (
    id int8 not null,
    name varchar(255) not null,
    parent_id int8,
    primary key (id)
);

create table comment (
    id int8 not null,
    text varchar(500) not null,
    author_id int8 not null,
    receiver_id int8 not null,
    time timestamp not null,
    rating int4 not null,
    primary key (id)
);

create table payment (
    id int8 not null,
    amount int4 not null,
    by_phone boolean,
    by_card boolean,
    receiver_id int8 not null,
    primary key (id)
);

alter table if exists task
    add constraint task_user_fk
    foreign key (user_id) references usr;

alter table if exists task
    add constraint task_category_fk
    foreign key (category_id) references category;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;

alter table if exists contract
    add constraint contract_task_fk
    foreign key (task_id) references task;

alter table if exists contract
    add constraint contract_user_fk
    foreign key (user_id) references usr;

alter table if exists category
    add constraint category_category_fk
    foreign key (parent_id) references category;

alter table if exists comment
    add constraint comment_author_fk
    foreign key (author_id) references usr;

alter table if exists comment
    add constraint comment_receiver_fk
    foreign key (receiver_id) references usr;

alter table if exists payment
    add constraint payment_user_fk
    foreign key (receiver_id) references usr;