insert into usr (id, username, password, email, active)
    values (1, 'Анатолий', '1', 'bugrovtolik@gmail.com', true);

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');