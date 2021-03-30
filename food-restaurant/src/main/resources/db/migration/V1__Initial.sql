CREATE TABLE category
(
   id            INT AUTO_INCREMENT PRIMARY KEY,
   code          VARCHAR(50) UNIQUE,
   description   VARCHAR(50) NOT NULL,
   created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   last_updated  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=INNODB;

INSERT INTO category(code, description)
VALUES('pizza', 'Pizza');
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
   id           INT AUTO_INCREMENT PRIMARY KEY,
   category_id  INT NOT NULL,
   name         VARCHAR(100) NOT NULL,
   logo         VARCHAR(255) NOT NULL,
   description  VARCHAR(150) NOT NULL,
   address      VARCHAR(100) NOT NULL,
   FOREIGN KEY (category_id) REFERENCES category(id)
);

ALTER TABLE restaurant ADD CONSTRAINT unique_restaurant UNIQUE (id, category_id);
