/* Exercice 1 */
/* 1° Que permet d'obtenir la requête ? */
/*
La liste des étudiants régulièrement inscrits au module ayant pour intitulé "EDBD";
*/

/* 2° Pour chaque plan d'exécution logique, calculer le nombre de lignes intermédiaires créées. */
/*
200 étudiants,
70 modules,
4200 IP (inscriptions pédagogiques),
50 formations,
70 IA (des étudiants peuvent être inscrits à plusieurs formations).

A1 = 
A2 = 
A3 = 
A4 =
*/

/* 3° Quel est le plan d'exécution logique optimal ? Pourquoi ? */
/*
Le plan le plus optimisé est le plan n°3, parce qu'il restraint le nombre de module, et par la suite il présente des jointures qui ne concerne que les tables nécéssaires minimum.
*/

/* Exercice 2 */
/*
Pour chacune des requêtes ci-dessous :
- indiquer ce qu'elles permettent d'obtenir,
- donner différents plans d'exécution logique,
- indiquer le plan optimal.
*/
/* Requete 1° */
/*
SELECT RESPONSABLE
FROM ETUDIANTS E ,MODULES M ,IP I
WHERE E.IDE = I.IDE AND M.IDM=I.IDM
AND NOM = "DUPOND" AND INTULE LIKE "HMIN";
*/
/*
Permet d'obtenir les noms des responsables des modules dont l'étudiant "Dupond" inscrit dans les modules commençant par "HMIN".
*/

/* Requete 2° */
/*
SELECT NOM
FROM ETUDIANTS E ,FORMATION F ,IA A, IP I, MODULE M
WHERE E.IDE = A.IDE AND F.IDF=A.IDF AND I.IDE=E.IDE AND M.IDM=I.IDM,
AND NOMF = "MASTER AIGLE" AND INTULE = "EDBD" ;
*/
/*
Permet d'obtenir les noms des étudiants du module "EDBD" de la formation "Master Aigle" pour les étudiants inscrits administrativement.
*/

/* Requete 3° */
/*
SELECT INTILULE
FROM ETUDIANTS E ,MODULES M ,IP I
WHERE E.IDE = I.IDE AND M.IDM=I.IDM
AND AGE = (select min (AGE) from ETUDIANTS) ;
*/
/*
Affiche les intitulés des modules dont les étudiants inscrits ont le plus petit âge de tous les étudiants.
*/

/* Exercice 3 */
/*
SELECT NOM
FROM JOURNAL, JOURNALISTE
WHERE TITRE='Le Monde' AND IDJ=IDJOURNALISTE AND PRENOM='Jean' ;
*/
/*°1 */
/*
Les deux expressions retournent-elles le même résultat (sont-elles équivalentes) ? Justifiez votre
réponse en indiquant les règles de réécriture que l'on peut appliquer.
*/
/*
Oui, elles sont équivalentes.
On peut descendre ou monter les conditions sur les selections et on retrouve les mêmes expressions.
*/

/* 2° */
/*
Une expression vous semble-t-elle meilleure que l'autre si on les considère comme des plans
d'exécution ?
*/
/*
La seconde expression est meilleure étant donnée que la jointure se fait sur des tables réduites.
*/

/* Exercice 4 */
/*
SELECT acteur.nom,acteur.prenom
FROM acteur,jouer,film,genre, realisateur
WHERE (idA=idActeur) AND (idFilm=idF) AND (idGenre=idG) AND (idRealisateur=idR)
AND (nationalite='France') AND (description='comédie') AND (realisateur.nom = "Les frères Coen") ;
*/
/*
Pour la requête ci-dessus, donner TOUS les plans d’exécution logique.
*/
/*
1°: Proj:acteur.nom,acteur.prenom( Selec:nationalite='France', description='comédie', realisateur=.nom='Les frères Coen'(acteur >< (idA=idActeur) jouer >< (idF=idFilm) film >< (idG=idGenre) genre >< (idR=idRealisateur) realisateur))
*/
/*
Parmi les plan d’exécution , quel est le plan optimal ? Justifier votre choix.
*/