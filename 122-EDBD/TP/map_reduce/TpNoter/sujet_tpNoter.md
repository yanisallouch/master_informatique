TP Noté Hadoop / Map-Reduce - (27/11 - 14/12)

TP Noté Hadoop/Map-Reduce, en binômes à rendre pour le 22/12

On vous demande d'implémenter en Map/Reduce des traitements sur un jeu de données inspiré de l'entrepôt de données que vous avez proposé pour votre mini-projet.


Tâches à réaliser :

    Créer un jeu de données correspondant à votre modèle d'entrepôt
        Vous pouvez utiliser Mockaroo pour la génération des données en CSV.
            Pour la génération des clés étrangères de la table de faits F vers une table dimensionnelle D vous pouvez utiliser l'option random(1,n) avec n la taille (nombre de lignes) de D.
            Pour un meilleur débogage, il est recommandé de commencer avec une dizaine de lignes par table.

    Implémenter 10 requêtes analytiques différentes en Map/Reduce. Ces requêtes doivent être intéressantes du point de vue de l'analyse des données, mais aussi montrer votre maîtrise de Map/Reduce. Vous pouvez choisir des traitements que vous avez déjà proposés dans le mini-projet mais également proposer des nouveaux traitements. Soyez créatifs !

    Implémenter et utiliser la technique de jointure par hachage pour l'évaluation des requêtes sur vos schémas en étoile. (voir suite)
        Évaluer la différence, en terme de temps d'exécution, avec l'approche qui consiste à enchaîner plusieurs job Map/Reduce.


Enfin, il sera bien évidemment nécessaire de corriger les dernières erreurs de modélisation (eg. des mesures dans les dimensions) ou encore celles relatives aux interrogations que vous avez proposées dans le mini-projet avant de commencer ce travail.



Jointure par hachage

Il s'agit d'une technique classique de jointure, qui devient toutefois particulièrement intéressante en map/reduce lorsqu'on se retrouve avec plusieurs relations à joindre. Un cas typique est celui des requêtes sur des schémas "en étoile" propres aux entrepôts de données, comme dans l'exemple suivant.

DimClient(idClient, …)   JOIN   FaitVente(idClient, idProduit, …)  JOIN   DimProduit(idProduit, …)


L'idée de cet algorithme de jointure est de diviser son évaluation dans un certain nombre de sous-tâches indépendantes et exécutables en parallèle.

|  tâche1    tâche2    tâche3 |

|  tâche4    tâche5    tâche6 |

|  tâche7    tâche8    tâche9 |

|  tâche10  tâche11   tâche12 |

|  tâche13  tâche14   tâche15 |

Cet algorithme est intéressant car au lieu d'avoir 1 seule tâche qui réalise un traitement sur l'ensemble de données (Clients, Produits, Ventes), nous avons (a) plusieurs tâches qui travaillent en parallèle et (b) chaque tâche travaille sur une portion des données. Une sous-tâche calcule donc un résultat partiel. La concaténation des résultats partiels donne enfin le résultat de la jointure.


Deux questions importantes pour comprendre la méthode :

    comment choisir le nombre de sous-tâches ?

    comment distribuer les données dans les sous-tâches ?      (on verra que la redondance est bienvenue)


#1 (conte-intuitif, mais) l'ingrédient de base est un partitionnement des colonnes impliquées dans la jointure (si besoin, relisez plusieurs fois cette phrase)


Par exemple, on peut supposer que les valeurs de la colonne idClient soient partitionnées en 5 ensembles, et les valeurs de la colonne idProduit en 3 ensembles.

colonne idClient       colonne idProduit

clients_partition_1      produits_partition_1

clients_partition_2      produits_partition_2

clients_partition_3      produits_partition_3

clients_partition_4

clients_partition_5

Une fonction auxiliaire de hachage (ou de coloration) hA  associe chaque valeur de la colonne A à sa partition (d'où le nom de cette technique).

Par exemple, hidProduit(P132H67) = 2.

Lire : l'identifiant de produit P132H67 de la colonne idProduit est associé à la partition 2.



#2 C'est presque fini. En effet, le partitionnement nous donne un tableau définissant les sous-tâches de la jointure.

|  clients_p_1,produits_p_1   clients_p_1,produits_p_2   clients_p_1,produits_p_3  |

|  clients_p_2,produits_p_1   clients_p_2,produits_p_2   clients_p_2,produits_p_3  |

|  clients_p_3,produits_p_1   clients_p_3,produits_p_2   clients_p_3,produits_p_3  |

|  clients_p_4,produits_p_1   clients_p_4,produits_p_2   clients_p_4,produits_p_3  |

|  clients_p_5,produits_p_1   clients_p_5,produits_p_2   clients_p_5,produits_p_3  |


Chaque sous-tâche travaille sur des combinaisons de valeurs (v1 ,v2,…,vk ) pour l'ensemble des colonnes (A1,A2 ,…,Ak) impliquées dans la jointure.

Ici le tableau est à 2 dimensions car on considère 2 attributs de jointures.

La taille du tableau correspond au nombre total de sous–tâches (clés en map reduce).



#3 Voyons maintenant les fonctions map et reduce.

Reduce (la plus simple des deux) : reçoit une sous partie des tables DimClient,   FaitVente, et  DimProduit et calcule la jointure.


Map : doit se comporter différemment en fonction de chaque table.

Reprenons notre exemple et faisons une analyse des cas.

DimClient(idClient , …)   JOIN   FaitVente( idClient , idProduit, …)  JOIN    DimProduit (idProduit, …)

- Une ligne de la table FaitVente

(idC,idP,…) est associée à la sous-tâche <hidClient(idC),hidProduit(idP)>

par exemple la ligne (C1123,P132H67,…) est associée à la sous tâche <4,2>


|  clients_p_1,produits_p_1   clients_p_1,produits_p_2   clients_p_1,produits_p_3  |

|  clients_p_2,produits_p_1   clients_p_2,produits_p_2   clients_p_2,produits_p_3  |

|  clients_p_3,produits_p_1   clients_p_3,produits_p_2   clients_p_3,produits_p_3  |

|  clients_p_4,produits_p_1   clients_p_4,produits_p_2   clients_p_4,produits_p_3  |

|  clients_p_5,produits_p_1   clients_p_5,produits_p_2   clients_p_5,produits_p_3  |

- Une ligne de la table DimClient

(idC,…) est par contre associée aux sous-tâches

<hidClient(idC),1>

<hidClient(idC),2>

<hidClient(idC),3>

car la colonne idProduit n'apparait pas dans la table DimClient


Par exemple la ligne (C1123,…) est associée aux sous tâches   <4,1> <4,2> <4,3>

|  clients_p_1,produits_p_1   clients_p_1,produits_p_2   clients_p_1,produits_p_3   |

|  clients_p_2,produits_p_1   clients_p_2,produits_p_2   clients_p_2,produits_p_3   |

|  clients_p_3,produits_p_1   clients_p_3,produits_p_2   clients_p_3,produits_p_3   |

|  clients_p_4,produits_p_1   clients_p_4,produits_p_2    clients_p_4,produits_p_3   |

|  clients_p_5,produits_p_1   clients_p_5,produits_p_2   clients_p_5,produits_p_3   |

- De même, une ligne de la table DimProduit

(idP,…) est associée aux sous tâches

<1,hidProduit(idP)>

<2,hidProduit(idP)>

<3,hidProduit(idP)>

<4,hidProduit(idP)>

<5,hidProduit(idP)>

car la colonne idClient n'apparait pas dans la table DimProduit


Par exemple la ligne (P132H67,…) est associée aux sous-tâches  <1,2> <2,2> <3,2> <4,2> <5,2>

|  clients_p_1,produits_p_1   clients_p_1,produits_p_2    clients_p_1,produits_p_3  |

|  clients_p_2,produits_p_1   clients_p_2,produits_p_2   clients_p_2,produits_p_3   |

|  clients_p_3,produits_p_1   clients_p_3,produits_p_2   clients_p_3,produits_p_3   |

|  clients_p_4,produits_p_1   clients_p_4,produits_p_2    clients_p_4,produits_p_3   |

|  clients_p_5,produits_p_1   clients_p_5,produits_p_2    clients_p_5,produits_p_3  |


Modifié le: vendredi 4 décembre 2020, 15:00