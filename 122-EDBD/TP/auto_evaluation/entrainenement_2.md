# Questions ouvertes

* Quelle est la différence entre une base de données relationnelles et un entrepôt de données ?
	- Le modèle relationnel se distingue par une absence de normalisation entre la table de fait et ces dimensions, qu'on appelle modèle en étoile qui peut même se transformer en constellation (relation entre les dimensions des étoiles).
	- La différence se posent aussi sur les objectifs et le workload des requêtes SQL. On distingue deux types OLTP et OLAP.
	- Éventuellement le manque de jointure dans les requêtes OLAP étant donné que l'information nécessaire se trouve dans la table de fait.

* Pourquoi les bases de données relationnelles ne sont pas adaptées à la gestion des données massives ?
	- La quantité de données n'est pas la même.
	- La rigueur des modèles pensées pour accueillir les données est très rigides.
	- Fonctionne comme une seule entité du propriété ACID qui impacte les performances et est coûteuse pour rester compétitive dans le traitement des données qui croit toujours plus vite.

* Pourquoi a-t-on introduit les plateformes de Big-Data ? Quels sont les avantages et les inconvénients par rapport aux entrepôts de données ?
	- Elles ont été introduites pour pouvoir prendre en main un flot de données structurées (relationnel) plus importants, mais aussi principalement les données semi-structurées ou mêmes non structurées.
	- Le datawarehouse contient des données historiques majoritairement qui sont raffinés, qui proviennent d'un écosystème assez homogènes a la différence des plateformes Big-Data.
	- Ensuite ça serait le temps de conservation qui est différent, mais peut être le même.
	- Enfin le gros inconvéniant, c'est le manque de système distribué dû à l'essence même des propriétés ACID qui empêche de traiter en temps raisonnable des quantités gigantesque de données.

* Pourquoi est-il nécessaire d’optimiser l’évaluation des requêtes dans les bases de données relationnelles ? Illustrez l’intérêt de l’optimisation avec un exemple.
	- Une optimisation de l'évaluation des requêtes permet de ne travailler que sur les données utiles. Par exemple, un **SELECT id, nom FROM foo, bar WHERE foo.id = bar.id;** n'a pas besoin de faire une jointure sur l'ensemble des colonnes. Le SGBD peut rapidement proposer une projection de la colonnes id et nom et ensuite faire la jointure. Si de plus nous devions restreindre certains noms, il est évident qu'elle se fera juste avant la jointure.

* Quelle est la différence entre les approches schema-on-read et schema-on-write ?
	- Le schéma se définit par les besoins réels des utilisateurs et s'adapte à ce besoin sur le temps. Je cite le titre du papier de Michael Stonebraker qui décrit bien le problème<<One Size Fits All": An Idea Whose Time Has Come and Gone>>.

* Quelle est la différence entre les approches d'analyse descriptive et prédictive des données ? Quelles technologies sont associées à chaque type d'analyse ?
	- L'analyse descriptive travaille sur des données brutes, les transforme et atteint une conclusion qui nous donne des conclusions sur le passé. La régression, association rule discovery, classification, clustering en Python & R.
	- L'analyse prédictive, travaille sur des données propres, s'entraîne sur des models préparés a l'avance, pour déduire des tendances futures. On est sur de l'Artificial Intelligence and Machine Learning

* Illustrer par des exemples les problématiques de volume, vélocité, et variété des données. Quelles solutions ont été proposées pour chaque problématique ?
	- Le volume désigne la quantité absurde de donnée récolté. Le stockage s'est démocratisé vers des solutions peu coûteuses et qui permettent facilement de stocker ce volume.
	- La vélocité désigne la vitesse a laquelle la quantité de données est récolté. Il y a des patterns mise en place par des algorithmes de réseaux de neurones qui permettent de faire le tri rapidement.
	- La variété désigne l'hétérogénéité de la donnée récoltée. Des solutions comme nosql, hadoop, qui s'affranchis des contraintes du format "document".

* Quelles sont les mesures de complexité d'un programme map reduce ? Pourquoi la complexité en temps ne suffit pas ?
	- Le MR est un processus parallélisé, les temps d'exécution peuvent alors varier énormément puisque des threads risquent d'attendre d'autres threads bloquants. Ensuite, une variable à prendre en compte sur de gros jeux de donnée est l'I/O speeds. Comme Google le précise, le réseau, et même en pratique et l'accès disque malgré les optimisations fourni par GFS est limitant.
	- De plus, même si on devait faire une analyse algorithmique classique, elle ressemblerait a cela : O(n log n * s * (1/p)) avec n le nombre de données, s le nombre de nœuds de l'architecture et p le ping, avec l'hypothèse qu'il est équivalent au sein de l'architecture.
	- Une analyse en temps et espace dépend de l'implémentation, de l'architecture et des algorithmes mise en place.
	- Enfin, MR n'a pas de comportement par défaut, a analyser ... Cela n'est pas logique.

* Quel est l'espace de stockage réellement occupé par un fichier de 1TB stocké dans Hadoop ? Justifier.
	- L'espace réellement occupé par un fichier de 1TB est de 2TB parce qu'il est lu et traité par les taches MAP puis copier et lu par les taches REDUCE qui produiront alors un résultat.

* Quel composant hardware des serveurs a évolué beaucoup moins rapidement que les autres en terme de performances pour l'accès aux données ?
	- Le disque dur est le composant matérielles qui a seulement évolués sur la quantité, on retrouve des Terabytes par disque, mais n'a pas fait d'amélioration sur la vitesse d'écriture et lecture, en moyenne limitée a 10000rpm.

* Décrire un cas d'utilisation pour les entrepôts de données et un cas d'utilisation pour Hadoop.
	- Le DW peut être utilisé pour étudier l'historique des transactions d'une enseigne de commerce du type Casino pour faire ressortir des tendances auprès de ces clients.
	- Hadoop peut être utiliser conjointement a la première solution pour analyser les commentaires des clients vis a vis de l'enseigne, des achats etc.

* Quelle est la différence entre ETL et ELT ?
	- Extract, Transform, Load, cela correspond au processus habituel pour un DW. Pour ne disposer que de données propres.
	- Extract, Load, Transform, correspondent plutôt au processus d'une plateforme de Big-Data. Pour raffiner toute la donnée plus tard. Cette dernière connaît du succès auprès des entreprises qui peut alors exploiter encore plus de données, plus vite.