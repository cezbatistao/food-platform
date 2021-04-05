CREATE TABLE category
(
   id            INT AUTO_INCREMENT PRIMARY KEY,
   uuid          BINARY(36) UNIQUE,
   code          VARCHAR(50) UNIQUE,
   description   VARCHAR(50) NOT NULL,
   created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   last_updated  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=INNODB;

INSERT INTO category(uuid, code, description)
VALUES('0eda194c-827c-4254-ada8-214115310fc6', 'pizza', 'Pizzaria');
INSERT INTO category(uuid, code, description)
VALUES('cc0cd1b9-4217-498d-8da8-661de03b86e5', 'hamburguer', 'Hambúrguer');
INSERT INTO category(uuid, code, description)
VALUES('577a9962-e9b7-46a9-abc8-211e42f6f1aa', 'japonesa', 'Japonesa');
INSERT INTO category(uuid, code, description)
VALUES('67e92973-f1ff-4cc3-b2c0-6485939ae442', 'vegetariana', 'Vegetariana');
INSERT INTO category(uuid, code, description)
VALUES('2358fb2d-f0dc-4a00-8b55-6fbbce158e91', 'brasileira', 'Brasileira');

CREATE TABLE restaurant
(
   id            INT AUTO_INCREMENT PRIMARY KEY,
   uuid          BINARY(36) UNIQUE,
   name          VARCHAR(100) NOT NULL,
   category_id   INT NOT NULL,
   logo          VARCHAR(255) NOT NULL,
   description   VARCHAR(300) NOT NULL,
   address       VARCHAR(100) NOT NULL,
   created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   last_updated  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (category_id) REFERENCES category(id)
) ENGINE=INNODB;

ALTER TABLE restaurant ADD CONSTRAINT unique_restaurant UNIQUE (id, category_id);

INSERT INTO restaurant(uuid, name, category_id, logo, description, address)
VALUES(
  'cbb9c2bd-abde-48a3-891a-6229fc9b7c2f',
  'Pizza Hut',
  (SELECT id FROM category WHERE code = 'pizza'),
  'https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg',
  'Pizza Hut é uma cadeia de restaurantes e franquias especializada em pizzas e massas. Com sede na cidade de Plano, no Texas, a Pizza Hut é a maior cadeia de pizzarias do mundo, com quase 15 mil restaurantes e quiosques em mais de 130 países. Possui 95 restaurantes no Brasil e 91 em Portugal.',
  'Av. Nome da avenida, 123'
);
INSERT INTO restaurant(uuid, name, category_id, logo, description, address)
VALUES(
  '23e0211c-19ea-47ee-b98e-77e023e1a95f',
  'Bráz Pizzaria',
  (SELECT id FROM category WHERE code = 'pizza'),
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwwg9Nscz6dkUtIpPUB0u1tAxfZmJcmcfGTA&usqp=CAU',
  'Pizzaria reúne ingredientes clássicos e sofisticados e muito burburinho e aconchego frente aos fornos à lenha.',
  'Av. Nome da outra avenida, 789'
);
INSERT INTO restaurant(uuid, name, category_id, logo, description, address)
VALUES(
  '36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c',
  'Domino\'s Pizza',
  (SELECT id FROM category WHERE code = 'pizza'),
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd7Ow6kgZoZk4h4oybgOkRJf6LZ_NLpBhfRA&usqp=CAU',
  'Domino\'s Pizza é uma empresa de alimentação baseada em pizzas. Atualmente, é a maior rede de entregas de pizzas do mundo, com 13.000 lojas em 83 países',
  'Rua Nome da rua, 654'
);

CREATE TABLE restaurant_menu_item
(
   id             INT AUTO_INCREMENT PRIMARY KEY,
   uuid           BINARY(36) UNIQUE,
   name           VARCHAR(50) NOT NULL,
   description    VARCHAR(150) NOT NULL,
   price          DECIMAL(10,2)  NOT NULL,
   restaurant_id  INT NOT NULL,
   created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   last_updated   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
) ENGINE=INNODB;

ALTER TABLE restaurant_menu_item ADD CONSTRAINT unique_restaurant_menu_item UNIQUE (id, restaurant_id);
