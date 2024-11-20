CREATE TABLE menus (
    id UUID PRIMARY KEY,
    reference_date DATE NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
