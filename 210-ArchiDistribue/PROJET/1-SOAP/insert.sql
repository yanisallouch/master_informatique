-- TypeChambre
insert into TypeChambre (identifiant, nblit) values (1, 1);
insert into TypeChambre (identifiant, nblit) values (2, 2);
insert into TypeChambre (identifiant, nblit) values (3, 3);
-- Adresse
insert into Adresse (identifiant, numero, rue, pays, positiongps, lieudit) values (1, 54, 'Rue Agence 1', 'France', '43.6°N, 3.9°E', 'Lieu dit Agence 1');
insert into Adresse (identifiant, numero, rue, pays, positiongps, lieudit) values (2, 22, 'Rue Agence 2', 'France', '43.6°N, 3.9°E', 'Lieu dit Agence 2');
insert into Adresse (identifiant, numero, rue, pays, positiongps, lieudit) values (3, 67, 'Rue Agence 3', 'France', '43.6°N, 3.9°E', 'Lieu dit Agence 3');
insert into Adresse (identifiant, numero, rue, pays, positiongps, lieudit) values (4, 123, 'Rue Hotel 1', 'France', '43.6°N, 3.9°E', 'Lieu dit Hotel 1');
insert into Adresse (identifiant, numero, rue, pays, positiongps, lieudit) values (5, 213, 'Rue Hotel 2', 'France', '43.6°N, 3.9°E', 'Lieu dit Hotel 2');
insert into Adresse (identifiant, numero, rue, pays, positiongps, lieudit) values (6, 312, 'Rue Hotel 3', 'France', '43.6°N, 3.9°E', 'Lieu dit Hotel 3');
-- Client
insert into Client (identifiant, nom, prenom) values (1, 'Whellams', 'Maëlys');
insert into Client (identifiant, nom, prenom) values (2, 'Wrathall', 'Kallisté');
-- Hotel
insert into Hotel (identifiant, nom, nbetoile, adresse) values (1, 'Hotel1', 5, 4);
insert into Hotel (identifiant, nom, nbetoile, adresse) values (2, 'Hotel2', 4, 5);
insert into Hotel (identifiant, nom, nbetoile, adresse) values (3, 'Hotel3', 3, 6);
-- Agence
insert into Agence (identifiant, nom, login, motdepasse, pourcentagereduction, adresse, idHotel) values (1, 'Kshlerin-Breitenberg', 'LoginAgence1', "123", 9, 1, 1);
insert into Agence (identifiant, nom, login, motdepasse, pourcentagereduction, adresse, idHotel) values (2, 'Bashirian, Goyette and Denesik', 'LoginAgence2', "123", 32, 2, 2);
insert into Agence (identifiant, nom, login, motdepasse, pourcentagereduction, adresse, idHotel) values (3, 'Hyatt-Corwin', 'LoginAgence3', "123", 27, 3, 3);
-- Chambre
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (1, 1, true, '01/03/2021', 51, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\1_lit.png", 1, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (2, 15, true, '10/03/2021', 41, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\1_lit.png", 1, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (3, 12, true, '10/04/2021', 69, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\1_lit.png", 1, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (4, 22, true, '02/03/2021', 50, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\2_lit.png", 2, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (5, 25, true, '20/03/2021', 79, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\2_lit.png", 2, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (6, 28, true, '02/04/2021', 38, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\2_lit.png", 2, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (7, 32, true, '02/03/2021', 69, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\3_lit.png", 3, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (8, 35, true, '18/03/2021', 57, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\3_lit.png", 3, 1);
insert into Chambre (identifiant, numero, estlibre, datedisponibilite, prixdebase, urlimage, typechambre, idHotel) values (9, 38, true, '31/03/2021', 82, "E:\gitlab.com\gestionhoteldistribueparagencedevoyage\ProjetGestionDonneeHotel\ProjetGestionDonneeHotel\assets\3_lit.png", 3, 1);
-- Reservation
insert into Reservation (identifiant, reference, datedebut, datefin, nbpersonne, cartecredit, client, agence, chambre) values (1, '#79744', '02/01/2021', '04/01/2021', 2, '4716533460348581', 1, 1, 1);
insert into Reservation (identifiant, reference, datedebut, datefin, nbpersonne, cartecredit, client, agence, chambre) values (2, '#98449', '02/02/2021', '04/02/2021', 2, '4716533460348581', 2, 1, 2);