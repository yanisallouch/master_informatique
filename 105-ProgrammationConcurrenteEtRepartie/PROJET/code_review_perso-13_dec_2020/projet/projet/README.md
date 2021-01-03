# Compiler

Se mettre au même niveau d'arborescence que le dossier [./bin](./bin) et [./src](./src)
Puis executer la commande suivante qui compile tout les fichiers sources de [./src](./src) et place les binaire dans [./bin](./bin)

		$ make

# Exécuter

Ensuite on peut facilement executer un binaire compiler précédemment avec des parametres prédéfini dans le make, avec la commande suivante

		$ make <nomBinaire>

### Exemple 1 :

		$ make serveurconcurrent

ou alors

		$ make client2

Le serveur prends en charge une initialisation par défaut écrite en dur dans le code.
Si vous choississez de fournir votre propre jeu de donnée. Vous avez la possibilités de tout taper dans le terminal via STDIN.

On recommande d'écrire en amont vos données dans un fichier de configuration, suivant la syntaxe spécifié plus bas. Puis de rediriger votre fichier sur STDIN via un **pipe**.

### Exemple 2 :

Vous souhaiter lancer le serveur avec les valeurs par défaut (10 villes différentes inspirées par le sujet données), alors vous ferez :

		$ make serveurconcurrent

ou bien vous souhaitez faire une saisie manuel des sites au clavier pendant l'exécution, alors vous ferez :

		$ make serveurconcurrent MANUEL=1

ou encore via un fichier de configuration localiser au même niveau :

		$ cat <configFile> | make serveurconcurrent MANUEL=1

### Remarque :

* Il possible aussi de faire la commande suivante qui produit le même résultat, en indiquant respectivement le port, le fichier à utiliser pour générer une clé.

		$ ./bin/ServeurConcurrent <port> <fichierCle> 1

## Jeu de donnée pré-défini :

On fournit 3 jeux de données, "[5sites](./5sites)", "[50sites](./50sites)", "[1024sites](./1024sites)"  respectivement un pour tester très rapidement sur 5 sites, un second avec 50 sites et enfin un fichier pour tester la limite hard codée de 1024 sites.

Pour finir sur les fichiers pré-définis, on fournit deux fichiers vides "[cleServeur](./bin/cleServeur)" et "[cleClient](./bin/cleClient)" qui servent simplement a générer une clé unique via l'appel a la fonction **ftok()**, ils ne sont pas très important, et peuvent être modifiés par les votres lors de l'éxécution en spécifiant la variable *CLE*.

### Exemple 3 :

		$ make serveurconcurrent CLESERVEUR=<fichier>
		$ make client2 CLECLIENT=<fichier>

# Housekeeping

On fournit deux nettoyage différent avec le *makefile* :

*	nettoyer :
	- Supprime les **binaires** situés dans ./bin et **fichiers temporaires** élus par les patterns suivant : {\*.o; \*.\*~}

*	propre :
	- Réutilise **nettoyer**, puis supprimes les clés prédéfinies avec les binaires et les recrées.

# Syntaxe pour les fichiers de configurations

Un fichier de configuration ressemble à ceci :

		5
		Mtp 12 45
		Lyo 43 12
		Occ 55 343
		Par 65 23
		Bor 123 23

Plus précisement, la première ligne indique le nombre de ligne suivante. Puis chaque ligne décrit d'abord le nom du site, puis le nombre de **CPU TOTAL** et enfin le nombre de **STOCKAGE TOTAL**.