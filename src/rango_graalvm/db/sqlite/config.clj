(ns rango-graalvm.db.sqlite.config)

(def schemas ["CREATE TABLE customers (id TEXT PRIMARY KEY, username TEXT NOT NULL, name TEXT, hashed_password TEXT NOT NULL)"
              "CREATE TABLE roles (id TEXT PRIMARY KEY, customer_id TEXT NOT NULL, role TEXT NOT NULL)"
              "CREATE TABLE menus (id UUID PRIMARY KEY, reference_date DATE NOT NULL, description TEXT NOT NULL, created_at TIMESTAMP NOT NULL)"
              "CREATE TABLE reservations (id UUID PRIMARY KEY, student_id UUID NOT NULL, menu_id UUID NOT NULL, created_at TIMESTAMP NOT NULL)"
              "CREATE TABLE students (id UUID PRIMARY KEY, code VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, class VARCHAR(255) NOT NULL, created_at TIMESTAMP NOT NULL)"])
