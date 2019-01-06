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

alter table if exists contract
    add constraint contract_task_fk
    foreign key (task_id) references task;

alter table if exists contract
    add constraint contract_user_fk
    foreign key (user_id) references usr;