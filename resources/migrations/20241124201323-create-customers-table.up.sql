CREATE TABLE customers (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    hashed_password VARCHAR(255) NOT NULL
);
