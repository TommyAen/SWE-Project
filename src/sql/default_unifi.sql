-- SQL script to create the database schema for the UNIFI car sharing system

-- reset
DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Trip;
DROP TABLE IF EXISTS Vehicle;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS "User";

-- TODO: da aggiungere constraints ?

-- Create tables
CREATE TABLE "User" (
    id INTEGER primary key, -- TODO: mettere limite a caratteri matricolo
    name VARCHAR(50),
    surname VARCHAR(50),
    email VARCHAR(40) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL, -- FIXME: fare hash??
    role VARCHAR(15) NOT NULL DEFAULT 'STUDENT',
    license VARCHAR(30) UNIQUE -- driver's license number: FIXME: mettere in un altra tabella??
);

CREATE TABLE Location (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    name VARCHAR(100) NOT NULL UNIQUE ,
    address VARCHAR(100) NOT NULL UNIQUE ,
    parking_spots INTEGER CONSTRAINT positive_spots CHECK (parking_spots >= 0) DEFAULT 2
);

CREATE TABLE Vehicle (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    capacity INTEGER NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, IN_USE, MAINTENANCE
    location INTEGER NOT NULL ,
    FOREIGN KEY (location) REFERENCES Location(id) ON DELETE CASCADE
);


CREATE TABLE Trip (
    id INTEGER GENERATED Always As IDENTITY primary key NOT NULL,
    origin INTEGER NOT NULL,
    destination INTEGER NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, COMPLETED, CANCELLED
    driver INTEGER , --TODO: da decidere come gestire creazione trip
    vehicle INTEGER NOT NULL,
    FOREIGN KEY (origin) REFERENCES Location(id),
    FOREIGN KEY (destination) REFERENCES Location(id),
    FOREIGN KEY (driver) REFERENCES "User"(id) ON DELETE SET NULL,
    FOREIGN KEY (vehicle) REFERENCES Vehicle(id)
);

CREATE TABLE Booking (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    trip INTEGER NOT NULL, -- trips must be created before bookings
    state VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, CONFIRMED, CANCELLED, COMPLETED
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE,
    FOREIGN KEY (trip) REFERENCES Trip(id) ON DELETE CASCADE
);

-- TODO: decidere come fare le cascade (se un utente viene cancellato, cancellare anche i suoi booking? ecc.. per tutte le tabelle)



INSERT INTO Location (name, address) VALUES
 ('Rettorato - UNIFI', 'Piazza San Marco, 4, 50121 Firenze FI'),
 ('Facoltà di Agraria', 'Piazzale delle Cascine, 18, 50144 Firenze FI'),
 ('Facoltà di Architettura', 'Via Micheli, 2, 50121 Firenze FI'),
 ('Economia / Giurisprudenza / Scienze Matematiche', 'Via delle Pandette, 32, 50127 Firenze FI'),
 ('Farmacia / Medicina e Polo Biomedico', 'Viale Morgagni, 40-44 / 85, 50134 Firenze FI'),
 ('Facoltà di Ingegneria', 'Via Santa Marta, 3, 50139 Firenze FI'),
 ('Centro Didattico Morgagni', 'Viale Morgagni, 65, 50134 Firenze FI'),
 ('Lettere e Filosofia', 'Piazza Brunelleschi, 3-4, 50121 Firenze FI'),
 ('Psicologia', 'Via della Torretta, 16, 50137 Firenze FI'),
 ('Scienze della Formazione', 'Via del Parione, 7, 50123 Firenze FI'),
 ('Polo delle Scienze Sociali (Novoli)', 'Piazza Ugo di Toscana, 3-5, 50127 Firenze FI'),
 ('Polo Scientifico - Sesto Fiorentino', 'Sesto Fiorentino, Firenze FI');
