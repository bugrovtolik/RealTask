insert into usr (id, username, password, email, active, credit, credit_card_number, phone_number)
    values (1, 'Анатолий', '1', 'bugrovtolik@gmail.com', true, 0, '4149629310933759', '0939117714');

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');