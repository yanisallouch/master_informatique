Se mettre au même niveau d'arborescence que le dossier ./bin et ./src
Puis executer la commande suivante qui compile tout les fichiers sources de ./src et place les binaire dans ./bin

		make

Ensuite on peut facilement executer un binaire avec des parametres prédéini dans le make la commande suivante

		make <nomBinaire>

Exemple :

		make serveurconcurrent

ou

		make client

Le serveur prends en charge une initialisation par défaut.

		make serveurconcurrent

ou bien la saisie manuel des sites

		make serveurconcurrent MANUEL=1

ou via un fichier de configuration

		cat configFile | make serveurconcurrent MANUEL=1
