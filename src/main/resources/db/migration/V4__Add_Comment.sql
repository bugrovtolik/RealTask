create table comment (
    id int8 not null,
    user_id int8 not null,
    task_id int8 not null,
    posted timestamp not null,
    text varchar(255),
    primary key (id)
);

alter table if exists comment
    add constraint comment_task_fk
    foreign key (task_id) references task;

alter table if exists comment
    add constraint comment_user_fk
    foreign key (user_id) references usr;