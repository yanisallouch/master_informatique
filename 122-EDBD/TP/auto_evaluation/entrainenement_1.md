# Un entrepôt de données pour une entreprise de télécommunications
## Question 1

Une statistique d’appel doit contenir les informations suivantes.

* L’émetteur (numéro ou identifiant de l'appelant)
* Le destinataire (numéro ou identifiant de l'appelé)
* La date et l'heure du début d'appel
* La durée d'appel
* Le type d'appel (Mobile, SMS, Fax, Fixe, ...)
* Le compte facturé pour l’appel (attention : pas toujours l’émetteur)
* Un identifiant unique pour chaque statistique d’appel
* Un code correspondant au dysfonctionnement/erreur éventuellement produit pendant l’appel

Travail demandé :

1. Proposer un schéma logique et physique pour l'entrepôt de données
2. Justifier la (semi,non)-additivité des mesures
3. Proposer 5 requêtes analytiques pertinentes
4. Estimer la taille de l'entrepôt sur 10 ans

#### Schéma logique / physique

J'ai déterminer les dimensions:

* Clients
* Emetteur // permet de géré les numéros non Bouygues
* Destinataire // avec une colonne sur l'opérateur eméttant/recevant l'appel et si bouygues on est sur qu'il est dans notre dimensions Clients :D
* Temps
* Dates
* Erreurs (DD : Dimension Dégénérer)
* Identifiant de statistique (DD)

J'en détermine le fait :

* Communications/Appels

#### Mesures

J'observe qu'une seule mesure _durée d'appel_ qui est additive sur toutes les dimensions proposés. Peut-etre que sur la dim. erreur c'est un peu plus tordu alors elle serait non-additive.

#### Requêtes analytiques

1. En moyenne combien d'SMS échangés par mois ?
2. Quels est l'augmentation du volume d'échange pendant les fêtes de fin d'années ?
3. Quels est le type d'appel que mes clients enregistre le plus ?
4. Quels est la panne la plus réccurente ? variante sur les types d'appels...
5. A quels heures de la journée mes clients sont le plus actifs ? variante par types d'appels...

#### Estimation

On sait que Bouygues possèdes 10 millions de clients, sur une durée de 10 ans, s'échangeant 100 millions d'appels par an (choix arbitraire étant donnée qu'il manque cette information), 5 colonnes mutilplié par 10 octects par colonnes de la tables de fait (très grosse approximation).

> 100M * 365 * 10 * 50 = 18.25 To

Sachant que la table de fait est la plus importante, je néglige les dimensions (1% du total si on est pessimiste).

## Question 2

Une facture doit contenir les informations suivantes.

* Le compte client (on considère que chaque client dispose d’un seul numéro)
* la période de référence
* Le coût (mensuel) des abonnements souscrits
* Le coût total des communications effectuées (on ne considère que trois types de communications : mobile, livebox, et poste fixe)
	* Incluses dans le forfait
	* Hors forfait
* Un identifiant unique pour chaque facture

Travail demandé :

1. Proposer un schéma logique et physique pour l'entrepôt de données
2. Justifier la (semi,non)-additivité des mesures
3. Proposer 5 requêtes analytiques pertinentes
4. Estimer la taille de l'entrepôt sur 10 ans

#### Schéma logique / physique

J'ai déterminer les dimensions:

* Clients
* Dates

J'en détermine le fait :

* Factures

#### Mesures

J'observe les mesures suivantes :

* Le coût (mensuel) des abonnements souscrits
* Le coût total des communications effectuées

Le coût (mensuel) des abonnements souscrits est une mesure additive sur toutes les dimensions.

Le coût total des communications effectuées est une messure additive sur toutes les dimensions.

#### Requêtes analytiques

1. Pendant noel, quel est le total de client qui ont vu le total du cout de leur abonnements augmenté ?
2. Quels sont les periodes de l'année qui enregistre un couts total plus élevé ?
3. Quels période de l'année possèède le plus de client ?
4. En moyenne, mes clients fidèles (+2 ans d'historique supposé continu) dépense combien en abonnements mensuels ?
5. Et par rapport au derniers mois ?

#### Estimation

On sait que Bouygues possèdes 10 millions de clients, sur une durée de 10 ans, aa raison d'une facture par mois pour 12 mois par an, 3 colonnes mutilplié par 10 octects par colonnes de la tables de fait (très grosse approximation).

> 10M * 120 * 30 = 36 Go

# Hierarchies

## 1) Proposez un schéma d’entrepôt de données permettant les analyses suivantes.

* compter le nombre de photos réalisées pour chaque appareil photo ;
* compter le nombre de photos prises par un appareil conçu par Eiji Fumio ;
* pour tous les modèles dérivés du Nikon D3000, compter le nombre de photos réalisées par jour
* indiquer les modèles historiquement les plus influents (on considère ici les appareils au sommets des hiérarchies de lignage).

# Questions ouvertes

* Quelle est la différence entre une base de données relationnelles et un entrepôt de données ?
	- Le modèle relationnel d'un DW se distingue par une absence de normalisation entre la table de fait et ces dimensions, qu'on appelle modèle en étoile qui peut même se transformer en constellation (relation entre les dimensions des étoiles).
	- La différence se posent aussi sur les objectifs et le workload des requêtes SQL. On distingue deux types OLTP : online transactional processing et OLAP : online analytics processing.
	- Éventuellement le manque de jointure dans les requêtes OLAP étant donné que l'information nécessaire se trouve dans la table de fait.

* Pourquoi les bases de données relationnelles ne sont pas adaptées à la gestion des données massives ?
	- La quantité de données n'est pas la même.
	- La rigueur des modèles pensées pour accueillir les données est très rigides.
	- Fonctionne comme une seule entité du à ACID qui impacte les performances et est coûteuse pour rester compétitive dans le traitement des données qui croit toujours plus vite.

* Pourquoi a-t-on introduit les plateformes de Big-Data ? Quels sont les avantages et les inconvénients par rapport aux entrepôts de données ?
	- Elles ont été introduites pour pouvoir prendre en main un flot de données structurées (relationnel) plus importants, mais surtout les données semi-structurées ou mêmes non structurées.
	- Le datawarehouse contient des données historiques majoritairement qui sont raffinés, qui proviennent d'un écosystème assez homogènes a la différence des plateformes Big-Data.
	- Ensuite ça serait le temps de conservation qui est différent, mais peut être le même.
	- Enfin le gros inconvéniant, c'est le manque de système distribué dû à l'essence même des propriétés ACID qui empêche de traiter en temps raisonnable des quantités gigantesque de données.

* Pourquoi est-il nécessaire d’optimiser l’évaluation des requêtes dans les bases de données relationnelles ? Illustrez l’intérêt de l’optimisation avec un exemple.
	- Une optimisation de l'évaluation des requêtes permet de ne travailler que sur les données utiles. Par exemple, un **SELECT id, nom FROM foo, bar WHERE foo.id = bar.id;** n'a pas besoin de faire une jointure sur l'ensemble des colonnes. Le SGBD peut rapidement proposer une projection de la colonnes id et nom et ensuite faire la jointure. Si de plus nous devions restreindre certains noms, il est évident qu'elle se fera juste avant la jointure.
	- Il y a aussi les joins multiple.

* Quelle est la différence entre les approches schema-on-read et schema-on-write ?
	- Le schéma se définit par les besoins réels des utilisateurs et s'adapte à ce besoin sur le temps. Je cite le titre du papier de Michael Stonebraker qui décrit bien le problème<<One Size Fits All": An Idea Whose Time Has Come and Gone>>.

* Quelle est la différence entre les approches d'analyse descriptive et prédictive des données ? Quelles technologies sont associées à chaque type d'analyse ?
	- L'analyse descriptive travaille sur des données brutes, les transforme et atteint une conclusion qui nous donne des conclusions sur le passé. La régression, association rule discovery, classification, clustering en Python & R.
	- L'analyse prédictive, travaille sur des données propres, s'entraîne sur des models préparés a l'avance, pour déduire des tendances futures. On est sur de l'Artificial Intelligence and Machine Learning.

* Illustrer par des exemples les problématiques de volume, vélocité, et variété des données. Quelles solutions ont été proposées pour chaque problématique ?
	- Le volume désigne la quantité absurde de donnée récolté. Le stockage s'est démocratisé vers des solutions peu coûteuses et qui permettent facilement de stocker ce volume. La solution Hadoop depuis 2000.
	- La vélocité désigne la vitesse a laquelle la quantité de données est récolté. Il y a des patterns mise en place par des algorithmes de réseaux de neurones qui permettent de faire le tri rapidement.
	- La variété désigne l'hétérogénéité de la donnée récoltée. Des solutions comme nosql, hadoop, qui s'affranchis des contraintes du format "document".

* Quelles sont les mesures de complexité d'un programme map reduce ? Pourquoi la complexité en temps ne suffit pas ?
	- Le MR est un processus parallélisé, les temps d'exécution peuvent alors varier énormément puisque des threads risquent d'attendre d'autres threads bloquants. Ensuite, une variable à prendre en compte sur de gros jeux de donnée est l'I/O speeds. Comme Google le précise, le réseau, et même en pratique et l'accès disque malgré les optimisations fourni par GFS est limitant.
	- De plus, même si on devait faire une analyse algorithmique classique, elle ressemblerait par exemple à cela : O(n log n * s ) avec n le nombre de données, s le nombre de nœuds de l'architectures, avec l'hypothèse qu'il est équivalent au sein de l'architecture.
	- Une analyse en temps et espace dépend de l'implémentation, de l'architecture et des algorithmes mise en place.
	- Enfin, MR n'a pas de comportement par défaut, a analyser ... Cela n'est pas logique.

* Quel est l'espace de stockage réellement occupé par un fichier de 1TB stocké dans Hadoop ? Justifier.
	- L'espace réellement occupé par un fichier de 1TB est de 3TB stocké via 3 replica géré par GFS. De plus l'exécution d'un MR produit des données supplémentaires (intermédiaires et final).

* Quel composant hardware des serveurs a évolué beaucoup moins rapidement que les autres en terme de performances pour l'accès aux données ?
	- Le disque dur est le composant matérielles qui a seulement évolués sur la quantité, on retrouve des Terabytes par disque, mais n'a pas fait d'amélioration sur la vitesse d'écriture et lecture, en moyenne a 10000rpm.

* Décrire un cas d'utilisation pour les entrepôts de données et un cas d'utilisation pour Hadoop.
	- Le DW peut être utilisé pour étudier l'historique des transactions d'une enseigne de commerce du type Casino pour faire ressortir des tendances auprès de ces clients.
	- Hadoop peut être utiliser conjointement a la première solution pour analyser les commentaires sur twitter des clients vis a vis de l'enseigne, des achats etc.

* Quelle est la différence entre ETL et ELT ?
	- Extract, Transform, Load, cela correspond au processus habituel pour un DW. Pour ne disposer que de données propres.
	- Extract, Load, Transform, correspondent plutôt au processus d'une plateforme de Big-Data. Pour raffiner toute la donnée plus tard. Cette dernière connaît du succès auprès des entreprises qui peut alors exploiter encore plus de données, plus vite.