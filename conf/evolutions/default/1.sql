# Products schema

# --- !Ups

CREATE TABLE product (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE stock (
    id INT NOT NULL AUTO_INCREMENT,
    product_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reserved_until TIMESTAMP NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE sale (
    id INT NOT NULL AUTO_INCREMENT,
    stock_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (stock_id) REFERENCES stock (id)
);

INSERT INTO product (id, name) VALUES (1, 'Motorbike');
INSERT INTO product (id, name) VALUES (2, 'Car');

INSERT INTO stock (id, product_id) VALUES (1, 1);
INSERT INTO stock (id, product_id) VALUES (2, 1);
INSERT INTO stock (id, product_id) VALUES (3, 1);
INSERT INTO stock (id, product_id) VALUES (4, 1);

INSERT INTO stock (id, product_id) VALUES (5, 2);
INSERT INTO stock (id, product_id) VALUES (6, 2);

INSERT INTO sale (stock_id) VALUES (3);

# --- !Downs

DROP TABLE product;
DROP TABLE stock;
DROP TABLE sale;
