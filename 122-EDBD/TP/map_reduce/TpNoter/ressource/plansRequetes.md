1. chiffres d'affaires par produit et par lieu
	déjà fait

2. chiffres d'affaires par produit et par mois (jointure après le group by) 2 tâches
	1. group by
		input: production
	2. join
		input: output du 1. + dates

3. chiffres d'affaires par produit et par semaine (jointure avant le group by) 2 tâches
	1. jointure pour récupérer le numéro de semaine:
		input: production et dates
	2. group by
		input: output de 1.


4. pays qui générent les plus gros chiffres d'affaires: 2 tâches
	1. group by sur l'id de lieux
		input: productions
	2. join sur l'id de lieux. pour avoir la somme des chiffres d'affaires (TreeMap<country,somme>)
		input: output de 1. + lieux


			Stocks
5. produits dont les stocks connaissent de fortes variations pour une période donnée, 1 tâche
	group by sur l'id produit, on fait e/s dans le map, on sélectionne les bonnes semaines dans le map, on somme dans le reduce
	input: stocks


6. Quels produits, relativement à leur demande moyenne, sont en pénurie où proches de l’être par continent? 2 tâches
	1. calcul de la demande historique par produit et PAR LIEU (et pas par continent): moyenne de la quantité sortante. Dans le reduce on écrit également le nombre de valeurs qui ont contribué à cette moyenne
		input: stocks

	2. join sur lieu. On remplit un TreeMap<<continent,produit>, List<Pair<moyenne,nombre de valeurs>>. Dans le il faut calculer la moyenne des moyennes.
		input: output de 1. + lieux
.

7. quels sont les entrepots ayant le plus de produits en stock 1 tâche
	group by entrepôts. Dans le map on sélectionne seulement la date qu'on veut. On envoie 1 par produit différent (par ligne).
	input: stocks


8. quelle est la moyenne de livraisons par semaine et par continents 2 taches
	1. group by <lieu,date> on envoie 1 en sortie du map (après avoir vérifié le type). On fait la somme dans le reduce (wordcount)
		input: productions
	2. join + aggrégation. Join sur le lieu pour obtenir le continent. Sortie du reduce <<continent,date>, sommeSurLeContinent>.
		input: output de 1. + lieux
	3. join + aggrégation +. Join sur les dates pour obtenir la semaine. TreeMap<continent, HashMap<semaine,nombre>>. calcule somme(nombre)/(nbreSemaines) pour chaque continent.

9. taux d'incidence de perte:
taux = (quantité perdue)/(quantité vendue)
1  tache
	1. sortie du map <produit, nombreFlaggé>. NombreFlaggé = quantité perdue ou vendue selon le type.
	2. Dans le reduce on somme les quantités perdues, on somme les quantités vendues, et on divise.
		input: productions


10. Quelle est la rentabilité de chaque produit pour chaque continent
2 taches
	1. group by <produit,lieu>. On somme les coûts d'un côté, et les ventes d'un autre. Les pertes et coûts auront été flaggés dans le map.
		input: production
	2. join sur le lieu. On extrait le continent dans le map. TreeMap<<produit,continent>,Pair<sommeVentes,sommeCoûts>>. on fait le calcul sommeVentes/sommeCoûts pour obtenir le résultat attendu
		input: output de 1. + lieux.
