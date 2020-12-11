set sqlblanklines on;
SET PAGESIZE 40;
SET LINESIZE 150;

DROP TABLE Commentaire ;
DROP TABLE Discussion ;
DROP TABLE Aime ;
DROP TABLE Contient ;
DROP TABLE Tag ;
DROP TABLE Photographie ;
DROP TABLE Galerie ;
DROP TABLE Album ;
DROP TABLE CollectionPhotos ;
DROP TABLE ContenuNumerique ;
DROP TABLE Abonne ;
DROP TABLE Utilisateur ;
DROP TABLE Lieu ;
DROP TABLE Dates;
DROP TABLE Configuration ;
DROP TABLE Appareil ;

CREATE TABLE Appareil (
	idAppareil number(16) NOT NULL,
	marque varchar(20),
	modele varchar(20),
	CONSTRAINT PK_Appareil PRIMARY KEY (idAppareil)
);

CREATE TABLE Configuration (
	idConfig number(16) NOT NULL,
	idAppareil number(16) NOT NULL,
	ouvertureFoc number(5),
	tpsExpo number(5),
	flash varchar(10),
	distFoc number(5),

	CONSTRAINT PK_Configuration PRIMARY KEY (idConfig),
	CONSTRAINT FK_Configuration_Appareil FOREIGN KEY (idAppareil) REFERENCES Appareil(idAppareil)
);

CREATE TABLE Dates (
	idDate number(16),
	daytime DATE,
	CONSTRAINT PK_Date PRIMARY KEY (idDate)
);

CREATE TABLE Lieu (
	idLieu number(16) NOT NULL,
	latitude number(10,6),
	longitude number(10,6),
	ville varchar(64),
	dept varchar(32),
	pays varchar(32),
	codePostal varchar(10),

	CONSTRAINT PK_Lieu PRIMARY KEY (idLieu)
);

CREATE TABLE Utilisateur(
	idUtilisateur number(16) NOT NULL,

	CONSTRAINT PK_Utilisateur PRIMARY KEY (idUtilisateur)
);

CREATE TABLE Abonne (
	idUtilisateur1 number(16) NOT NULL,
	idUtilisateur2 number(16) NOT NULL,

	CONSTRAINT PK_Abonne PRIMARY KEY (idUtilisateur1, idUtilisateur2),
	CONSTRAINT FK_Abonne_Utilisateur1 FOREIGN KEY (idUtilisateur1) REFERENCES Utilisateur(idUtilisateur),
	CONSTRAINT FK_Abonne_Utilisateur2 FOREIGN KEY (idUtilisateur2) REFERENCES Utilisateur(idUtilisateur)
);

CREATE TABLE ContenuNumerique (
	idContenu number(16,0) NOT NULL,

	CONSTRAINT PK_ContenuNumerique PRIMARY KEY (idContenu)
);

CREATE TABLE CollectionPhotos (
	idCollection number(16) NOT NULL,

	CONSTRAINT PK_CollectionPhotos PRIMARY KEY (idCollection)
);

CREATE TABLE Album (
	idAlbum number(16) NOT NULL,
	idUtilisateur number(16) NOT NULL,

	CONSTRAINT PK_Album PRIMARY KEY (idAlbum),
	CONSTRAINT FK_Album_CollectionPhotos FOREIGN KEY (idAlbum) REFERENCES CollectionPhotos(idCollection),
	CONSTRAINT FK_Album_Utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur)
);

CREATE TABLE Galerie (
	idGalerie number(16) NOT NULL,
	idUtilisateur number(16) NOT NULL,

	CONSTRAINT PK_Gallerie PRIMARY KEY (idGalerie),
	CONSTRAINT FK_Galerie_CollectionPhotos FOREIGN KEY (idGalerie) REFERENCES CollectionPhotos(idCollection),
	CONSTRAINT FK_Galerie_Utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur)
);

CREATE TABLE Photographie (
	idPhoto number(16) NOT NULL,
	licence varchar(32) NOT NULL,
	idUtilisateur number(16) NOT NULL,
	idDatePrise number(16),
	idDatePubliee number(16),
	idLieu number(16),
	idConfig number(16) NOT NULL,
	idDiscussion number(16),
	CONSTRAINT PK_Photographie PRIMARY KEY (idPhoto),
	CONSTRAINT Check_licence CHECK ( licence IN ('TousDroitsReserves', 'UtilisationCommercialeAutorisee', 'ModificationImageAutorisee')),
	CONSTRAINT FK_Photographie_ContenuNumerique FOREIGN KEY (idPhoto) REFERENCES ContenuNumerique (idContenu),
	CONSTRAINT FK_Photographie_Utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur),
	CONSTRAINT FK_Photographie_DatePrise FOREIGN KEY (idDatePrise) REFERENCES Dates(idDate),
	CONSTRAINT FK_Photographie_DatePubliee FOREIGN KEY (idDatePubliee) REFERENCES Dates(idDate),
	CONSTRAINT FK_Photographie_Lieu FOREIGN KEY (idLieu) REFERENCES Lieu(idLieu),
	CONSTRAINT FK_Photographie_Configuration FOREIGN KEY (idConfig) REFERENCES Configuration(idConfig)
);

CREATE TABLE Tag (
	idTag number(16) NOT NULL,
	tagContenu varchar(32),
	idUtilisateur number(16),
	idPhoto number(16),
	CONSTRAINT FK_Tag_Utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur),
	CONSTRAINT FK_Tag_Photo FOREIGN KEY (idPhoto) REFERENCES Photographie(idPhoto),
	CONSTRAINT PK_Tag PRIMARY KEY (idTag)
);

CREATE TABLE Contient (
	idPhoto number(16) NOT NULL,
	idCollection number(16) NOT NULL,
	CONSTRAINT PK_Contient PRIMARY KEY (idPhoto, idCollection),
	CONSTRAINT FK_Contient_Photographie FOREIGN KEY (idPhoto) REFERENCES Photographie(idPhoto),
	CONSTRAINT FK_Contient_CollectionPhotos FOREIGN KEY (idCollection) REFERENCES CollectionPhotos(idCollection)
);

CREATE TABLE Aime (
	idUtilisateur number(16) NOT NULL,
	idPhoto number(16) NOT NULL,
	CONSTRAINT PK_Aime PRIMARY KEY (idUtilisateur, idPhoto),
	CONSTRAINT FK_Aime_Utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur),
	CONSTRAINT FK_Aime_Photographie FOREIGN KEY (idPhoto) REFERENCES Photographie(idPhoto)
);

CREATE TABLE Discussion (
	idDiscussion number(16) NOT NULL,
	idPhoto number(16) NOT NULL,
	CONSTRAINT PK_Discussion PRIMARY KEY (idDiscussion),
	CONSTRAINT FK_Discussion_Photographie FOREIGN KEY (idPhoto) REFERENCES Photographie(idPhoto)
);

CREATE TABLE Commentaire (
	idCommentaire number(16) NOT NULL,
	idDiscussion number(16) NOT NULL,
	idUtilisateur number(16) NOT NULL,
	texte varchar(256) NOT NULL,
	CONSTRAINT PK_Commentaire PRIMARY KEY (idCommentaire),
	CONSTRAINT FK_Commentaire_ContenuNumerique FOREIGN KEY (idCommentaire) REFERENCES ContenuNumerique(idContenu),
	CONSTRAINT FK_Commentaire_Discussion FOREIGN KEY (idDiscussion) REFERENCES Discussion(idDiscussion),
	CONSTRAINT FK_Commentaire_Utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur)
);