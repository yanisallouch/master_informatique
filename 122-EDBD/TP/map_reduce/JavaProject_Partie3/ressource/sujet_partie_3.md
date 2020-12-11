TP Hadoop / Map-Reduce - Partie 3 (27/11)

Exercice 10 - Tri

Vous avez peut être remarqué le fait qu'au niveau du résultat (fichiers output) Hadoop trie les clés des groupes en ordre lexicographique ascendant.

Plus précisément, ce tri est effectué pendant la phase de shuffling. Il y a donc une méthode qui effectue cette comparaison lexicographique que nous voulons modifier.

    Trier les commandes clients du fichier superstore.csv (dans input-groupBy) par date d’expédition en ordre croissant, puis décroissant (créer deux classes différentes).

    La classe TriAvecComparaison.java fournit un squelette de solution. La méthode compare permet de redéfinir l'opérateur de comparaison.

    Trier les clients (identifiant+nom) par profit généré

    Cette question demande de 1) enchaîner deux jobs map-reduce jobA et jobB dans la méthode main et 2) définir les fonctions mapJobA, reduceJobA, mapJobB, reduceJobB. L'entrée du deuxième job correspond à la sortie du premier job, qui est enregistrée dans un repertoire de sortie. Il faudra donc faire attention à bien indiquer où le deuxième job peut récupérer les données.


Exercice 11 - Requêtes Top-k

Soit k  une valeur de seuil.

La classe TopkWordCount.java permet de compter les k mots les plus fréquents d'un fichier de texte.

Noter que les k  mots les plus fréquents ne coincident pas forcement avec les mots dont la fréquence est au moins k . Par exemple, soit le texte "A A A B B C C C", où A, B, et C représentent des mots. Soit k =2. Les k-mots les plus fréquents sont A et C, mais tous les mots ont fréquence supérieure ou égale à 2.

Pour évaluer des requêtes top-k la fonction reduce doit évoluer par rapport aux exemples vus précédemment.

    La méthode reduce doit connaître la valeur du paramètre k donné en entrée. Le passage du paramètre se fait par une notion de contexte liée au job Map/Reduce. La méthode setup() permet de récupérer la valeur.
    Les différents appels à la méthode reduce doivent être au courant des fréquences calculées par les autres méthodes. Pour cela, on utilise des variables globales (sortedwords et nsortedwords) dans la méthode reduce.
    On doit attendre la fin de toutes les appels à la méthodes reduce pour écrire la sortie. Cela est fait par la méthode cleanup().

Modifier la classe TopkWordCount.java pour répondre aux requêtes suivantes.

    Donner les k premières lignes du fichier superstore.csv une fois celles-ci triées par profit (évidemment, en ordre décroissant).
    Donner les k premiers clients du fichier superstore.csv en terme de profit réalisé (ordre décroissant).


Exercice 12 - TAM (suite question 9)

Répondre aux questions suivantes :

    Quels sont les 10 stations les plus desservies par les tram sur la journée ?
    Quels sont les 10 stations les plus desservies par les bus ?
    Quels sont les 10 stations les plus desservies (tram et bus) ?


Modifié le: samedi 21 novembre 2020, 22:06