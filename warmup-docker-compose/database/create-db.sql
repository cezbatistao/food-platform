CREATE DATABASE IF NOT EXISTS db_dc_restaurant;
CREATE USER 'restaurant_dc_user'@'%' IDENTIFIED BY 'restaurant_dc_passwd' ;
GRANT ALL ON `db_dc_restaurant`.* TO 'restaurant_dc_user'@'%';

CREATE DATABASE IF NOT EXISTS db_dc_order;
CREATE USER 'order_dc_user'@'%' IDENTIFIED BY 'order_dc_passwd' ;
GRANT ALL ON `db_dc_order`.* TO 'order_dc_user'@'%';
