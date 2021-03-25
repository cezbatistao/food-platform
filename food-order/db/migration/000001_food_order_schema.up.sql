CREATE TABLE tb_order_payment (
    id serial PRIMARY KEY,
    uuid VARCHAR ( 36 ) UNIQUE NOT NULL,
    status VARCHAR ( 10 ) NOT NULL,
    transaction_id VARCHAR ( 36 ) NOT NULL
);

CREATE TABLE tb_order (
    id serial PRIMARY KEY,
    uuid VARCHAR ( 36 ) UNIQUE NOT NULL,
    user_uuid VARCHAR ( 36 ) NOT NULL,
    restaurant_uuid VARCHAR ( 36 ) NOT NULL,
    restaurant_name VARCHAR ( 128 ) NOT NULL,
    number INT NOT NULL,
    status VARCHAR ( 10 ) NOT NULL,
    total NUMERIC ( 10,2 ) NOT NULL,
    order_payment_id INT NULL REFERENCES tb_order_payment,
    created_at TIMESTAMP NOT NULL,
    last_updated TIMESTAMP  NOT NULL
);

CREATE TABLE tb_order_item (
    id serial PRIMARY KEY,
    order_id INT NOT NULL REFERENCES tb_order,
    uuid VARCHAR ( 36 ) UNIQUE NOT NULL,
    menu_item_uuid VARCHAR ( 36 ) NOT NULL,
    menu_item_name VARCHAR ( 128 ) NOT NULL,
    amount INT NOT NULL,
    unit_value NUMERIC ( 10,2 ) NOT NULL
);
