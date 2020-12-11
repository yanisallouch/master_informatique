#	TP Hadoop / Map-Reduce - Partie 2 (20/11)

Ressources

[MR1] MapReduce: Simplified Data Processing on Large Clusters - Jeffrey Dean and Sanjay Ghemawat

[MR2] Apache Hadoop http://hadoop.apache.org/

[MR3] Hadoop : the definitive guide (http://grut-computing.com/HadoopBook.pdf)



##	Exercice 6 - GroupBy + Join

	Modifiez le programme de l'exercice 5 afin de calculer le montant total des achats faits par chaque client.

	Le programme doit restituer des couples (CUSTOMERS.name, SUM(totalprice))


##	Exercice 7 - Suppression des doublons (DISTINCT)

	Donner la liste des clients (sans doublons) présents dans le dataset du répertoire input-groupBy


##	Exercice 8 - MR <-> SQL

	Donner le code SQL équivalent aux traitements Map/Reduce implémentés pour les questions 4, 5, 6 et 7.


##	Exercice 9 - TAM

Rendez vous à l'adresse http://data.montpellier3m.fr/dataset/offre-de-transport-tam-en-temps-reel et télécharger le fichier TAM_MMM_OffreJour.zip contenant la prévision du service de tramway pour la journée. Le contenu du fichier est décrit à la même page. La 5ème colonne contient le nom de la ligne (les trams correspondent aux lignes 1-4, les autres ce sont des bus).

Répondre aux questions suivantes :

    *	Donner un aperçu des trams et bus de la station OCCITANIE. Plus précisément, donner le nombre de (bus ou trams) pour chaque heure et ligne. Exemple : <Ligne 1, 17h, 30> (lire : à 17h, passent 30 tram de la ligne 1)

    *	Pour chaque station, donner le nombre de trams et bus par jour.

    *	Pour chaque station et chaque heure, afficher une information X_tram correspondant au trafic des trams, avec X_tram="faible" si au plus 8 trams sont prévus (noter qu'une ligne de circulation a deux sens, donc au plus 4 trams par heure et sens), X_tram="moyen" si entre 9 et 18 trams sont prévus, et X="fort" pour toute autre valeur.
    Afficher la même information pour les bus. Pour les stations où il a seulement des trams (ou des bus) il faut afficher une seule information. Optionnel : comment peut-on prendre en compte la direction des trams pour donner des informations plus précises ?



Modifié le: mercredi 18 novembre 2020, 17:31