DROP TABLE IF EXISTS ITEM;

CREATE TABLE ITEM (
   id INT IDENTITY  PRIMARY KEY,
   description VARCHAR(250) NOT NULL,
   price NUMBER DEFAULT '0.00'
);