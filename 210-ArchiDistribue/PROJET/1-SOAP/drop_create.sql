--
-- File generated with SQLiteStudio v3.3.2 on Thu Apr 8 20:21:42 2021
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Reservation
DROP TABLE IF EXISTS Reservation;
-- Table: Chambre
DROP TABLE IF EXISTS Chambre;
-- Table: Agence
DROP TABLE IF EXISTS Agence;
-- Table: Hotel
DROP TABLE IF EXISTS Hotel;
-- Table: Client
DROP TABLE IF EXISTS Client;
-- Table: Adresse
DROP TABLE IF EXISTS Adresse;
-- Table: TypeChambre
DROP TABLE IF EXISTS TypeChambre;

CREATE TABLE Adresse (
    Identifiant NUMERIC CONSTRAINT PK_ADRESSE PRIMARY KEY,
    Numero      NUMERIC,
    Rue         VARCHAR,
    Pays        VARCHAR,
    PositionGPS VARCHAR,
    LieuDit     VARCHAR
);

CREATE TABLE Hotel (
    Identifiant NUMERIC CONSTRAINT PK_HOTEL PRIMARY KEY,
    Nom         VARCHAR,
    NbEtoile    NUMERIC,
    Adresse     NUMERIC CONSTRAINT FK_HOTEL_ADRESSE REFERENCES Adresse (Identifiant)
);

CREATE TABLE TypeChambre (
    Identifiant NUMERIC CONSTRAINT PK_TYPE_CHAMBRE PRIMARY KEY,
    NbLit       NUMERIC
);

CREATE TABLE Chambre (
    Identifiant       NUMERIC         CONSTRAINT PK_CHAMBRE PRIMARY KEY,
    Numero            NUMERIC,
    EstLibre          BOOLEAN,
    DateDisponibilite VARCHAR,
    PrixDeBase        NUMERIC (10, 2),
    UrlImage          VARCHAR,
    TypeChambre       NUMERIC         CONSTRAINT FK_CHAMBRE_TYPE_CHAMBRE REFERENCES TypeChambre (Identifiant),
    Image             CHAR,
    idHotel           NUMERIC CONSTRAINT FK_CHAMBRE_HOTEL REFERENCES Hotel (Identifiant)
);

CREATE TABLE Client (
    Identifiant NUMERIC CONSTRAINT PK_CLIENT PRIMARY KEY,
    Nom         VARCHAR,
    Prenom      VARCHAR
);

CREATE TABLE Agence (
    Identifiant          NUMERIC        CONSTRAINT PK_AGENCE PRIMARY KEY,
    Nom                  VARCHAR,
    Login                VARCHAR,
    MotDePasse           VARCHAR,
    PourcentageReduction NUMERIC (3, 2),
    Adresse              NUMERIC        CONSTRAINT FK_AGENCE_ADRESSE REFERENCES Adresse (Identifiant),
    idHotel              NUMERIC CONSTRAINT FK_AGENCE_HOTEL REFERENCES Hotel (Identifiant) 
);

CREATE TABLE Reservation (
    Identifiant NUMERIC CONSTRAINT PK_RESERVATION PRIMARY KEY,
    Reference   VARCHAR,
    DateDebut   VARCHAR,
    DateFin     VARCHAR,
    NbPersonne  NUMERIC,
    CarteCredit VARCHAR,
    Client      NUMERIC CONSTRAINT FK_RESERVATION_CLIENT REFERENCES Client (Identifiant),
    Agence      NUMERIC CONSTRAINT FK_RESERVATION_AGENCE REFERENCES Agence (Identifiant),
    Chambre     NUMERIC CONSTRAINT FK_RESERVATION_CHAMBRE REFERENCES Chambre (Identifiant)
);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;