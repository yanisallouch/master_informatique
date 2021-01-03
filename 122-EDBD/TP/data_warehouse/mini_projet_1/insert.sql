INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('1', 'MG01', 'MG', 'LANTUS 100 UIml', 'LANTUS', '5,24', '5', '204', '80', '130', '40', '416');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('2', 'MG02', 'MG', 'LANTUS SOLOSTAR 100 UIml~', 'LANTUS', '7,56', '5', '291', '120', '170', '60', '1224');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('3', 'MG03', 'MG', 'TOUJEO SOLOSTAR 300 UIml', 'TOUJEO', '7,98', '3', '127', '120', '170', '60', '1224');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('4', 'MG04', 'MG', 'TOUJEO DOUBLESTAR 300 UIml', 'TOUJEO', '7,86', '3', '127', '120', '170', '60', '1224');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('5', 'MS01', 'MS', 'AUBAGIO', 'AUBAGIO', '56,35', '28', '85', '60', '90', '20', '108');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('6', 'MS02', 'MS', 'DUPIXENT 300 mg', 'DUPIXENT', '89,02', '1', '154', '70', '120', '20', '168');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('7', 'GP01', 'GP', 'DOLIPRANE 2,4 % SANS SUCRE', 'DOLIPRANE', '1,02', '1', '102', '60', '80', '40', '192');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('8', 'GP02', 'GP', 'DOLIPRANE 1000 mg adulte', 'DOLIPRANE', '0,87', '8', '85', '30', '70', '20', '42');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('9', 'GP03', 'GP', 'XYZALL', 'XYZALL', '1,13', '14', '', '70', '100', '40', '280');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('10', 'VA01', 'VA', 'FLUZONE HIGH-DOSE QUADRIVALENT', 'FLUZONE', '1,67', '1', '102', '60', '80', '40', '192');
INSERT INTO PRODUITS (id, idProduit, gbuType, nomProduit, gammeProduit, coutMoyen, venduParQuantite, poidsTotal, largeurEmballage, longueurEmballage, hauteurEmballage, volumeEmballage) VALUES('11', 'VA02', 'VA', 'IMOVAX POLIO', 'IMOVAX', '0,86', '1', '108', '60', '180', '40', '192');


INSERT INTO PRESTATAIRES (id, idInterne, nom, nomComplet, nomResponsable, indicatif, telephone, fonction) VALUES('1', '2020PSD-FOO', 'Foo', 'SANOFI', 'Fontaine C.', '7', '(922) 973-2013', 'non-defini');
INSERT INTO PRESTATAIRES (id, idInterne, nom, nomComplet, nomResponsable, indicatif, telephone, fonction) VALUES('2', '2016KKC-UPS', 'UPS', 'UPS', 'Rodriguez K.', '33', '07 38 58 27 87', 'livraison');
INSERT INTO PRESTATAIRES (id, idInterne, nom, nomComplet, nomResponsable, indicatif, telephone, fonction) VALUES('3', '2007REN-FED', 'Fedex', 'Federal Express', 'Schimmel A.', '1', '521-553-6292', 'livraison');

INSERT INTO LIEUX (id, nom, designationInterne, typeLieu, ville, codePostal, numAdresse, adresse, nbPersonnel) VALUES('1', 'entrepotAAA', 'FR-MTP-1', 'entrepot', 'montpellier', '34000', '349', 'avenue du marchE de gare', '30');
INSERT INTO LIEUX (id, nom, designationInterne, typeLieu, ville, codePostal, numAdresse, adresse, nbPersonnel) VALUES('2', 'usine947', 'FR-PARIS-23', 'usine', 'Evry-GrEgy-sur-Yerre', '94700', '0000', 'chemin du breuil', '160');
INSERT INTO LIEUX (id, nom, designationInterne, typeLieu, ville, codePostal, numAdresse, adresse, nbPersonnel) VALUES('3', 'entrepotBBB', 'RUS-SOFIA-1', 'entrepot', 'purus lorem', 'nulla sed sodales', '11', 'malesuada', '42');

INSERT INTO DATES_TABLE (idDate, dateSQL, dateComplete, jourSemaine, numSemaine, numMois, numJourAnnee) VALUES ('20201101' ,TO_DATE('20201101','YYYYMMDD') ,'01 novembre 2020' , 'dimanche','44' ,'11' ,'306');
INSERT INTO DATES_TABLE (idDate, dateSQL, dateComplete, jourSemaine, numSemaine, numMois, numJourAnnee)  VALUES  ('20201102' ,TO_DATE('20201102','YYYYMMDD') ,'02 novembre 2020' , 'lundi','45' ,'11' ,'307');
INSERT INTO DATES_TABLE (idDate, dateSQL, dateComplete, jourSemaine, numSemaine, numMois, numJourAnnee)  VALUES  ('20201103' ,TO_DATE('20201103','YYYYMMDD') ,'03 novembre 2020' , 'mardi','45' ,'11' ,'308' );
INSERT INTO DATES_TABLE (idDate, dateSQL, dateComplete, jourSemaine, numSemaine, numMois, numJourAnnee)  VALUES  ('20201106' ,TO_DATE('20201106','YYYYMMDD') ,'06 novembre 2020' , 'vendredi','45' ,'11' ,'311');
INSERT INTO DATES_TABLE (idDate, dateSQL, dateComplete, jourSemaine, numSemaine, numMois, numJourAnnee)  VALUES  ('20201113' ,TO_DATE('20201113','YYYYMMDD') ,'13 novembre 2020' , 'vendredi','46' ,'11' ,'318');

INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('1', '2', '2', '1', '20201101', '20201101', 'fabrication', '974', '4034,00');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('2', '2', '2', '1', '20201101', '20201101', 'fabrication', '3449', '1987987');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('3', '2', '2', '1', '20201101', '20201101', 'fabrication', '4657', '97636');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('4', '1', '3', '2', '20201101', '20201102', 'transit', '4132', '6458');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('5', '2', '2', '1', '20201101', '20201101', 'fabrication', '126456', '23178');

INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('6', '2', '2', '1', '20201101', '20201101', 'fabrication', '152646', '164867');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('7', '2', '2', '1', '20201101', '20201101', 'fabrication', '2464', '45678');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('8', '3', '1', '2', '20201101', '20201103', 'transit', '4567', '1262');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('9', '3', '3', '3', '20201101', '20201101', 'transit', '16146', '486978');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('10', '1', '3', '3', '20201101', '20201102', 'livraison', '1264', '7862');
INSERT INTO PRODUCTIONS (idProduit, idLieuDepart, idLieuArrivee, idPrestataire, idDateDebut, idDateFin, typeOperation, quantiteDeProduit, coutOperation) VALUES('3', '2', '2', '1', '20201101', '20201101', 'perte', '67', '0');


INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '1', '4300', '1200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '2', '2800', '600');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '3', '6700', '3100');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '4', '4500', '900');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '5', '0', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '6', '3500', '800');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '7', '5000', '600');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '8', '24000', '9000');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '9', '0', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '10', '0', '8500');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '1', '11', '2000', '100');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '1', '0', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '2', '1400', '200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '3', '3000', '800');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '4', '2300', '400');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '5', '6700', '800');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '6', '2800', '1200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '7', '2900', '700');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '8', '18000', '6900');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '9', '6000', '800');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '10', '52000', '25000');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201106', '2', '11', '900', '200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '1', '3900', '900');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '2', '2200', '1000');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '3', '9600', '4000');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '4', '4500', '600');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '5', '0', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '6', '2500', '500');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '7', '', '');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '8', '24000', '7600');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '9', '0', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '10', '32400', '6400');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '1', '11', '1900', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '1', '0', '0');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '2', '1100', '200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '3', '3700', '1200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '4', '2600', '200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '5', '6300', '1500');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '6', '2200', '900');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '7', '3200', '500');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '8', '21000', '6200');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '9', '5200', '500');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '10', '75000', '48000');
INSERT INTO STOCKS (idDateSnapshot, idEntrepot, idProduit, stock, quantiteSortieDurantLaPeriode) VALUES('20201113', '2', '11', '1200', '300');