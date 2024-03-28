
CREATE TABLE book_inventory (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price float
);



INSERT INTO book_inventory (name, price) VALUES ('The Great Gatsby', 15.99);
INSERT INTO book_inventory (name, price) VALUES ('To Kill a Mockingbird', 12.50);
INSERT INTO book_inventory (name, price) VALUES ('1984', 10.99);
INSERT INTO book_inventory (name, price) VALUES ('Pride and Prejudice', 14.25);
INSERT INTO book_inventory (name, price) VALUES ('The Catcher in the Rye', 11.75);


DELETE FROM book_inventory WHERE name = '1984';


UPDATE book_inventory SET price = 16.99 WHERE name = 'The Great Gatsby';

