
insert into orders (id, user_id, address_id, payment_id, total_amount, creation_date)
 values (1, 1, 1, 1 , 1, '2020-10-23');
insert into orders (id, user_id, address_id, payment_id, total_amount, creation_date)
 values (2, 2, 2, 2 , 2, '2020-10-24');

insert into order_details  (id, price, product_id, quantity, orders_id)
 values (1, 10, 5, 6, 1);
insert into order_details  (id, price, product_id, quantity, orders_id)
 values (2, 8, 5, 6, 1);
insert into order_details  (id, price, product_id, quantity, orders_id)
 values (3, 7, 2, 1, 2);
insert into order_details  (id, price, product_id, quantity, orders_id)
 values (4, 89, 5, 9, 2);