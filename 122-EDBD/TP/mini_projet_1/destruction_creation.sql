DROP TABLE PRODUCTIONS;
DROP TABLE STOCKS;
DROP TABLE PRODUITS;
DROP TABLE PRESTATAIRES;
DROP TABLE LIEUX;
DROP TABLE DATES_TABLE;



CREATE TABLE PRODUITS (
	id int PRIMARY KEY,
	idProduit varchar(38),
	nomProduit varchar(38),
	gammeProduit varchar(38),
	coutMoyen integer,
	gbuType varchar(5) ,
	venduParQuantite integer,
	poidsTotal integer,
	largeurEmballage integer,
	longueurEmballage integer,
	hauteurEmballage integer,
	volumeEmballage integer,
	constraint CHK_PRODUITS CHECK ( gbuType in ('MS', 'MG', 'VA','GP'))
);

CREATE TABLE PRESTATAIRES (
	id int  PRIMARY KEY,
	idInterne varchar(38),
	nom varchar(38),
	nomComplet varchar(38),
	nomResponsable varchar(38),
	indicatif varchar(38),
	telephone varchar(38),
	fonction varchar(38),
	constraint CHK_PRESTATAIRES CHECK (fonction in ('livraison', 'non-defini'))
);

CREATE TABLE LIEUX (
	id int PRIMARY KEY,
	idLieu int,
	nom varchar(38),
	designationInterne varchar(38),
	aireDeStockageEffectifEntrepot int,
	nbTotalPaletteStockable int,
	typeLieu varchar(38),
	hemisphere varchar(38),
	continent varchar(38),
	pays varchar(38),
	region varchar(38),
	departement varchar(38),
	ville varchar(38),
	codePostal varchar(38),
	numAdresse int,
	adresse varchar(38),
	indicationSupplementaire varchar(38),
	addresseComplete varchar(38),
	nbPersonnel int,
	constraint CHK_LIEUX_typeLieu CHECK (typeLieu in ('entrepot', 'usine', 'non-defini')),
	constraint CHK_LIEUX_hemisphere CHECK (hemisphere in ('nord', 'sud'))
);

CREATE OR REPLACE VIEW ENTREPOTS AS
SELECT id, idLieu as idEntrepot, nom  as  nomEntrepot, designationInterne, aireDeStockageEffectifEntrepot, nbTotalPaletteStockable, hemisphere, continent, pays, region, departement, ville, codePostal, numAdresse, adresse, indicationSupplementaire, addresseComplete, nbPersonnel
FROM LIEUX WHERE typeLieu = 'entrepot';

CREATE TABLE DATES_TABLE (
	idDate int PRIMARY KEY,
	dateSQL DATE,
	dateComplete varchar(38),
	jourSemaine varchar(38),
	numSemaine int,
	numMois int,
	numJourMois int,
	numJourAnnee int,
	numJourMoisFiscal int,
	numJourAnneFiscal int,
	dernierJourSemaine varchar(38),
	dernierJourMois varchar(38),
	dateFinSemaine varchar(38),
	numSemaineAnne int,
	nomMois varchar(38),
	numMoisAnnee int,
	dateFormatMM_AAAA varchar(38),
	numSemestre int,
	numTrimestre int,
	numAnnee int,
	numSemaineFiscal int,
	numSemaineFiscalAnnee int,
	numMoisFiscal int,
	numMoisFiscalAnnee int
);

CREATE TABLE PRODUCTIONS (
	idProduit number(8),
	idLieuDepart number(8),
	idLieuArrivee number(8),
	idPrestataire number(8),
	idDateDebut number(8),
	idDateFin number(8),
	typeOperation varchar(15),
	quantiteDeProduit number(8,2),
	coutOperation number(12,2),
	constraint FK_PRODUCTIONS_produit foreign key (idProduit) references PRODUITS (id),
	constraint FK_PRODUCTIONS_lieuDepart foreign key (idLieuDepart) references LIEUX (id),
	constraint FK_PRODUCTIONS_lieuArrivee foreign key (idLieuArrivee) references LIEUX (id),
	constraint FK_PRODUCTIONS_prestataire foreign key (idPrestataire) references PRESTATAIRES (id),
	constraint FK_PRODUCTIONS_dateDebut foreign key (idDateDebut) references DATES_TABLE (idDate),
	constraint FK_PRODUCTIONS_dateFin foreign key (idDateFin) references DATES_TABLE (idDate),
	constraint PK_PRODUCTIONS primary key (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation),
	constraint CHK_PRODUCTIONS_typeOperation CHECK (typeOperation in ('fabrication', 'transit', 'entrepot', 'vente', 'livraison', 'perte', 'non-defini'))
);

CREATE TABLE STOCKS (
	idDateSnapshot number(8),
	idEntrepot number(8),
	idProduit number(8),
	stock number(8,2),
	quantiteSortieDurantLaPeriode number(8,2),
	constraint FK_STOCKS_dateSnapshot foreign key (idDateSnapshot) references DATES_TABLE (idDate),
	constraint FK_STOCKS_entrepot foreign key (idEntrepot) references LIEUX (id),
	constraint FK_STOCKS_produit foreign key (idProduit) references PRODUITS (id)
);