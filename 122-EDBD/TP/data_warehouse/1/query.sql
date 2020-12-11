/* a) Imaginez 3 recherches puis les traduire en SQL. */
/* 	1)Les photos de Montpellier */

SELECT  P.idPhoto AS Montpellier
FROM    Photographie P, Lieu L 
WHERE   P.idLieu = L.idLieu
AND     L.ville LIKE 'Montpellier';

/* 2) Lister les albums de l'utilisateur 3. */

SELECT  A.idAlbum AS MesAlbums
FROM    Utilisateur U, Album A 
WHERE   U.idUtilisateur = A.idUtilisateur
AND     U.idUtilisateur = 3;

/* 3) Afficher le nombre d'utilisateur auquel l'utilisateur n°3 est abonné. */

SELECT  Count(*) AS nbAbonne
FROM    Utilisateur U,  Abonne A
WHERE   U.idUtilisateur = A.idUtilisateur1
AND     U.idUtilisateur = 3;

/* b) Donner la requete qui permet de trouver les photos les plus appreciees avec la licence de distribution 'tous droits reserves'. */

SELECT	P.idPhoto
FROM	Photographie P, Aime A, Utilisateur U
WHERE	P.licence LIKE 'TousDroitsReserves'
AND		P.idPhoto = A.idPhoto
AND		P.idUtilisateur =  U.idUtilisateur
GROUP	BY P.idPhoto
HAVING	COUNT(*)
		>= ALL(
			SELECT	COUNT(*)
			FROM	Photographie P1, Aime A1, Utilisateur U1
			WHERE	P1.licence LIKE 'TousDroitsReserves'
			AND		P1.idPhoto = A1.idPhoto
			AND		P1.idUtilisateur =  U1.idUtilisateur
			GROUP	BY P1.idPhoto
);

/* c) Donner la requête qui permet de trouver les photos incluses dans le plus
grande nombre de galeries.*/

SELECT	P.idPhoto AS PhotoLaPlusIncluse
FROM	Galerie G, Photographie P, Contient C, CollectionPhotos CP
WHERE	P.idPhoto = C.idPhoto
AND		C.idCollection = G.idGalerie
GROUP	BY P.idPhoto
HAVING	Count(*)
		>= ALL (
			SELECT	COUNT(*)
			FROM	Galerie G1, Photographie P1, Contient C1, CollectionPhotos CP1
			WHERE	P1.idPhoto = C1.idPhoto
			AND		C1.idCollection = G1.idGalerie
			GROUP	BY P1.idPhoto
		);