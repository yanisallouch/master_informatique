TP Hadoop / Map-Reduce - Partie 1 (13/11)

Ressources

[MR1](https://static.googleusercontent.com/media/research.google.com/en//archive/mapreduce-osdi04.pdf) MapReduce: Simplified Data Processing on Large Clusters - Jeffrey Dean and Sanjay Ghemawat

[MR2](http://hadoop.apache.org/) Apache Hadoop http://hadoop.apache.org/

[MR3](http://grut-computing.com/HadoopBook.pdf) Hadoop : the definitive guide (http://grut-computing.com/HadoopBook.pdf)


Avant commencer
La programmation en Map-Reduce utilise le langage Java - avec ses avantages et inconvénients.  Il est ainsi indispensable d'effectuer ce travail en binômes, de rester bien concentrés pour bien comprendre la cause des bogues (souvent ce seront des problèmes de typage ou nommage des ressources) et ne pas perdre du temps.

Il est vivement conseillé d'utiliser l'IDE Eclipse pour réaliser ce TP. L'archive dont vous disposerez à été pensée pour être facilement intégré avec cet environnement de développement. Nous détaillerons la procédure de préparation de votre environnement pour cet IDE.


Format du rendu

Il est demandé de rédiger un document expliquant en quelques lignes comment vous avez répondu à chaque question du TP. Il est important de mettre en évidence juste le points le plus importants vous ayant permis de répondre à la question. Le code Java produit est également à rendre, mais dans un archive .zip


Télécharger l'archive [TP_HMIN122M-hadoop.zip](https://www.dropbox.com/s/d68l5r2dntyr6h9/TP_HMIN122M-hadoop.zip?dl=0)

https://www.dropbox.com/s/d68l5r2dntyr6h9/TP_HMIN122M-hadoop.zip?dl=0


MapReduce avec Eclipse

Voici les étapes pour configurer Eclipse afin de pouvoir utiliser Map-Reduce :

    Décompressez l'archive en conservant le dossier racine TP_HMIN122M-hadoop/

    Dans le menu file, créez un nouveau «java projet» (dans java).

    Décochez la case "Use default location".
    Dans le champ "location" sélectionnez le chemin vers votre dossier TP_HMIN122M-hadoop/
    Cliquez sur "next".

    Puis cliquez sur l'onglet «Libraries», et assurez vous qu'Eclipse détecte bien 7 librairies en .jar dans "Classpath".

    Si ce n'est pas le cas cliquez  sur «Add external JARs» et naviguez pour vous rendre dans le dossier TP_HMIN122M-hadoop/lib-dev, puis ajoutez y tous les .jar qui y sont présents.

    Cliquez sur «finish» pour créer le projet.

    Maintenant, vous pouvez tester le programme src/WordCount.java à partir de Eclipse (voir le menu "Run", ou faire CTRL+F11 pour exécuter le fichier source courant, il est possible de lancer le debugguer avec F11)

    Après chaque exécution, les résultats sont enregistrés dans des sous-dossiers output/wordCount-xxxx. En cliquant sur «Refresh» (ou F5 dans le Package Explorer) vous pouvez voir les fichiers de résultats dans Eclipse. Vous pouvez aussi les voir en allant explorer directement le dossier via l'explorateur de fichier du système.

Installation alternative

En cas de difficulté avec la première méthode voici une alternative.

Il est possible de directement charger le projet à partir de l'archive.

    Dans le menu file cliquer sur "Open projects from File System"
    Cliquer sur "Archive..."
    Sélectionner TP_HMIN122M-hadoop.zip, puis cliquer sur "OK"
    Dans la liste "Folder"  qui s'affiche : déssélectionner la première ligne : "TP_HMIN122M-hadoop.zip_expanded"
    Cliquer sur "Finish"

Le projet "TP_HMIN122M-hadoop" s'ajoutera alors dans la fenêtre "Project Explorer". Les fichiers sur le disque seront localisés à la racine de votre workspace, vous pouvez les retrouver en faisant : clic droit sur votre projet/show in/System Explorer".

Problème sur les nouveaux postes informatique

L'exception levée sur les nouveaux postes informatiques est dûe au nouveau mécanisme d'authentification de l'UM, où votre nom utilisateur correspond à votre adresse e-mail ; Kerberos, le protocole d'authentification utilisé par Hadoop utilise des règles différentes pour les adresses email.

La solution est de modifier votre nom utilisateur lors de l'exécution du programme.
Pour ce faire il suffit d'ajouter la variable d'environnement HADOOP_USER_NAME avant l'exécution du programme.
Le plus simple est de l'ajouter directement via Eclipse où l'on va modifier la configuration à l'exécution.

    Faites un click droit sur la racine de votre projet, situé dans l'onglet à droite, suivez "Run as/Run Configurations"
    Onglet "Environment"
    New...
    Name: HADOOP_USER_NAME, Value: user

Exécutez le programme WordCount et vérifiez que tout fonctionne.


Bug de Hadoop sous Windows

L'utilisation de Hadoop sous Windows entraîne la levée d'une exception de type IOException (Failed to set permissions of path: \tmp\hadoop-user\mapred\staging\user722309568\.staging to 0700). C'est un bug connu de Hadoop (cf. [https://issues.apache.org/jira/browse/HADOOP-7682](https://issues.apache.org/jira/browse/HADOOP-7682)), qu'il est possible de résoudre de la manière suivante.

    Télécharger le jar suivant https://github.com/congainc/patch-hadoop_7682-1.0.x-win/downloads
    Déplacer le jar dans TP_HMIN122M-hadoop/lib-dev
    Ajouter le .jar au build path
        Dans le package explorer, clic droit sur le projet /Build Path/Configure Build Path/Librairies/Add external jars
        Ajouter le .jar précédemment téléchargé
    Changer une valeur de la configuration du job avec la méthode suivante
    conf.set("fs.file.impl", "com.conga.services.hadoop.patch.HADOOP_7682.WinLocalFileSystem");
    Où conf est l'objet Configuration envoyé au job lors de sa création.
    Exécuter le programme et vérifier son bon fonctionnement



FAQ

    Le log dans la sortie standard est illisible. Je ne comprends pas ce que fait mon programme Map-Reduce !

Réponse : Le programme lit les fichiers contenus dans le répertoire input-wordCount/, puis effectue un comptage des mots (vérifiez-le !)

Voici les statistiques les plus importantes (pour démarrer) visualisées lors de l'exécution d'un job Hadoop :

    Map input records
        Le nombre de couples clé-valeur traitées avec appels à la fonction map.

    Map output records
        Le nombre de couples clé-valeur produites par des appels à la fonction map.

    Reduce input records
        Le nombre de couples clé-valeur traitées avec appels à la fonction reduce. Typiquement égale à Map output records, mais plus petit si l'on active certaines optimisations.

    Reduce input groups
        Le nombre de clés distinctes traitées avec appels à la fonction reduce

    Reduce output records
        Le nombre de couples clé-valeur produites par des appels à la fonction reduce.


L'exemple hadoopPoem contient un texte sur 17 lignes composé par 72 mots dont 59 distinctes.
Un appel à la fonction map est réalisé pour chaque ligne du fichier. Nous avons donc Map input records = 17.
Une paire (clé,valeur) est générée par un mapper pour chaque mot présent dans la ligne. Nous avons donc Map output records = 72.
Toutes ces paires sont traitées par les reducers. Nous avons donc Reduce input records = 72.
Étant donné que le texte contient seulement 59 mots distinctes, nous avons Reduce input groups = 59.
Enfin, le reduce calcule le nombre d'occurence de chaque mot, et nous avons Reduce output records = 59.

Plus de détails : ici.

    Où sont les résultats de mon programme Map-Reduce --> voir le répertoire output/wordCount-xxxx

    Y a-t-il de la parallélisation à l'état actuel --> non, on utilisera Hadoop en mode Standalone pour ce TP (mais les plus courageux pourront essayer la version "pseudo-distributed"; pour la version "fully-distributed" on aurait besoin d'un cluster).

    Qu'est ce qu'on fait maintenant ? L'idée est de modifier les programmes fournis afin d'implémenter les traitements ci-dessous. Il faut donc d'abord chercher à comprendre le code.

    Important : comment puis-je déboguer mon programme ? Vous disposez d'un logger via la variable de classe 'LOG' qui permet d'envoyer des messages d'erreurs en console. Ces messages d'erreurs seront aussi enregistrés dans le fichier TP_HMIN122M-hadoop/out.log. De plus, Eclipse fournit un puissant debugger (Run/Debug ou F11) qui permet d’exécuter pas à pas le programme et de vérifier les valeurs des différentes variables à l'exécution.


Exercice 0 - WordCount

Tester le programme WordCount.


Exercice 1 - WordCount + Filter
Modifier la fonction reduce du programme WordCount.java afin que seulement les mots dont le nombre d’occurrences est supérieur ou égal à deux soient affichés.


Exercice 3 - Group-By

Vous devez compléter le code dans la classe GroupBy.java qui est fournie afin d'implémenter un opérateur de regroupement sur l'attribut Customer-ID du fichier de données fourni dans le répertoire input-groupBy. Il s'agit d'un fichier .csv dont les lignes contiennent des valeurs pour les attributs suivants :

Row ID,Order ID,Order Date,Ship Date,Ship Mode,Customer ID,Customer Name,Segment,Country,City,State,Postal Code,Region,Product ID,Category,Sub-Category,Product Name,Sales,Quantity,Discount,Profit

Voici un exemple de données :

1,CA-2016-152156,11/8/16,11/11/16,Second Class,CG-12520,Claire Gute,Consumer,United States,Henderson,Kentucky,42420,South,FUR-BO-10001798,Furniture,Bookcases,Bush Somerset Collection Bookcase,261.96,2,0,41.9136

La valeur du profit peut être négative si les produits on été vendus à perte.


Le programme doit calculer le montant total des achats faits par chaque client (colonne Profit)

Attention au typage des variables lorsque vous manipulez des objets de type Text, String, Float, WritableInt, etc. C'est le point de difficulté majeur. Utilisez une variable de type float pour calculer la somme des profits. Il s'agit de vraies données, il se peut donc que parmi les valeurs numériques se cachent des caractères.


Les méthodes Map et Reduce de WordCount.java demandent de spécifier le type des clés-valeurs en entrée (2 types) et en sortie (2 types).

Mapper<LongWritable, Text, Text, IntWritable>

Lire : Map prend en entrée une clé de type LongWritable et une valeur de type Text (= le contenu d'une ligne du fichier) et génère des couples clé-valeur dont la clé est de type Text (en effet, ce sera un mot de la phrase) et la valeur est de type IntWritable (la valeur 1 représentant l'occurrence du mot).

Comme l'entrée du Reducer est la sortie du Mapper, on retrouvera ces deux derniers types dans la méthode Reducer.

Reducer<Text, IntWritable, Text, IntWritable>

On a bien sûr le droit de changer les signatures des méthodes. Pour voir un exemple, comparez la classe WordCount.java avec GroupBy.java.


Exercice 4 - Group-By
Modifier le programme précédent afin de calculer le montant des ventes par Date et State.

Calculer le montant des ventes par Date et Category.

Pour chaque commande calculer 1) le nombre de produits différents (distincts) achètes, ainsi que 2) le nombre total d'exemplaires.


Exercice 5 - Join

Maintenant c'est à vous ! Sur la base des programmes WordCount.java et GroupBy.java, définir une classe Join.java permettant de joindre les lignes concernant les informations des clients et des commandes contenus dans le répertoire input-join.

La jointure doit être réalisée sur l'attribut custkey. Voici le schéma des relations dont les lignes sont extraites :

ORDERS(orderkey,custkey,orderstatuts,totalprice,orderdate,orderpriority,clerk,ship-priority,comment)

CUSTOMERS(custkey,name,address,nationkey,phone,acctbal,mktsegment,comment)

Le programme doit restituer des couples (CUSTOMERS.name,ORDERS.comment)

Note :

Pour réaliser la jointure il faut à l'avance recopier dans un tableau temporaire les valeurs de l'itérateur values dans la méthode REDUCE, puis effectuer le parcours avec deux 'for' imbriqués sur ce tableau temporaire.

Modifié le: vendredi 13 novembre 2020, 14:35