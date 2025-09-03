-- SQL script to create the database schema for the UNIFI car sharing system

-- reset
DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Trip;
DROP TABLE IF EXISTS Vehicle;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS "User";


-- Enums
-- FIXME: usare ENUM o VARCHARì?????
CREATE TYPE user_role AS ENUM ('STUDENT', 'ADMIN');
CREATE TYPE vehicle_state AS ENUM ('AVAILABLE', 'IN_USE', 'MAINTENANCE');
CREATE TYPE booking_state AS ENUM ('PENDING', 'CONFIRMED', 'CANCELLED');
CREATE TYPE trip_state AS ENUM ('SCHEDULED', 'COMPLETED', 'CANCELLED');

-- TODO: da aggiungere constraints

-- Create tables
CREATE TABLE "User" (
    id INTEGER primary key, -- TODO: mettere limite a caratteri matricolo
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER', -- USER, ADMIN
    license VARCHAR(50) -- driver's license number
);

CREATE TABLE Location (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    vehicle_capacity INTEGER CONSTRAINT positive_capacity CHECK (vehicle_capacity >= 0) DEFAULT 0
);

CREATE TABLE Vehicle (
    id INTEGER PRIMARY KEY,
    capacity INTEGER NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, IN_USE, MAINTENANCE
    location INTEGER NOT NULL ,
    FOREIGN KEY (location) REFERENCES Location(id)
);


CREATE TABLE Trip (
    id INTEGER GENERATED Always As IDENTITY primary key NOT NULL,
    init_loc INTEGER NOT NULL,
    dest_loc INTEGER NOT NULL,
    driver INTEGER NOT NULL, --TODO: da decidere come gestire creazione trip
    vehicle INTEGER NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (init_loc) REFERENCES Location(id),
    FOREIGN KEY (dest_loc) REFERENCES Location(id),
    FOREIGN KEY (driver) REFERENCES "User"(id),
    FOREIGN KEY (vehicle) REFERENCES Vehicle(id)
);

CREATE TABLE Booking (
    id INTEGER PRIMARY KEY,
    "user" INTEGER NOT NULL,
    trip INTEGER NOT NULL,
    date Date NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, CONFIRMED, CANCELLED, COMPLETED
    FOREIGN KEY ("user") REFERENCES "User"(id),
    FOREIGN KEY (trip) REFERENCES Trip(id)
);



INSERT INTO Location (name, address) VALUES
 ('Rettorato - UNIFI', 'Piazza San Marco, 4, 50121 Firenze FI'),
 ('Facoltà di Agraria', 'Piazzale delle Cascine, 18, 50144 Firenze FI'),
 ('Facoltà di Architettura', 'Via Micheli, 2, 50121 Firenze FI'),
 ('Economia / Giurisprudenza / Scienze Matematiche', 'Via delle Pandette, 32, 50127 Firenze FI'),
 ('Farmacia / Medicina e Polo Biomedico', 'Viale Morgagni, 40-44 / 85, 50134 Firenze FI'),
 ('Facoltà di Ingegneria', 'Via Santa Marta, 3, 50139 Firenze FI'),
 ('Lettere e Filosofia', 'Piazza Brunelleschi, 3-4, 50121 Firenze FI'),
 ('Psicologia', 'Via della Torretta, 16, 50137 Firenze FI'),
 ('Scienze della Formazione', 'Via del Parione, 7, 50123 Firenze FI'),
 ('Polo delle Scienze Sociali (Novoli)', 'Piazza Ugo di Toscana, 3-5, 50127 Firenze FI'),
 ('Polo Scientifico - Sesto Fiorentino', 'Sesto Fiorentino, Firenze FI');
