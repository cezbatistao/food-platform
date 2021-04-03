CREATE TABLE category
(
   id            INT AUTO_INCREMENT PRIMARY KEY,
   code          VARCHAR(50) UNIQUE,
   description   VARCHAR(50) NOT NULL,
   created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   last_updated  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=INNODB;

INSERT INTO category(code, description)
VALUES('pizza', 'Pizzaria');
INSERT INTO category(code, description)
VALUES('hamburguer', 'Hambúrguer');
INSERT INTO category(code, description)
VALUES('japonesa', 'Japonesa');
INSERT INTO category(code, description)
VALUES('vegetariana', 'Vegetariana');
INSERT INTO category(code, description)
VALUES('brasileira', 'Brasileira');

CREATE TABLE restaurant
(
   id            INT AUTO_INCREMENT PRIMARY KEY,
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

INSERT INTO restaurant(name, category_id, logo, description, address)
VALUES(
  'Pizza Hut',
  (SELECT id FROM category WHERE code = 'pizza'),
  'https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg',
  'Pizza Hut é uma cadeia de restaurantes e franquias especializada em pizzas e massas. Com sede na cidade de Plano, no Texas, a Pizza Hut é a maior cadeia de pizzarias do mundo, com quase 15 mil restaurantes e quiosques em mais de 130 países. Possui 95 restaurantes no Brasil e 91 em Portugal.',
  'Av. Nome da avenida, 123'
);
INSERT INTO restaurant(name, category_id, logo, description, address)
VALUES(
  'Bráz Pizzaria',
  (SELECT id FROM category WHERE code = 'pizza'),
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwwg9Nscz6dkUtIpPUB0u1tAxfZmJcmcfGTA&usqp=CAU',
  'Pizzaria reúne ingredientes clássicos e sofisticados e muito burburinho e aconchego frente aos fornos à lenha.',
  'Av. Nome da outra avenida, 789'
);
INSERT INTO restaurant(name, category_id, logo, description, address)
VALUES(
  'Domino\'s Pizza',
  (SELECT id FROM category WHERE code = 'pizza'),
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd7Ow6kgZoZk4h4oybgOkRJf6LZ_NLpBhfRA&usqp=CAU',
  'Domino\'s Pizza é uma empresa de alimentação baseada em pizzas. Atualmente, é a maior rede de entregas de pizzas do mundo, com 13.000 lojas em 83 países',
  'Rua Nome da rua, 654'
);
