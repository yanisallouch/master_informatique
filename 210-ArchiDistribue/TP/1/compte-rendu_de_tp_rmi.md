# Q1. Expliquez quel est le rôle de la classe de stub/proxy RMI, et donc de ses instances.

## Ahmed & Azzedine

La classe stub implémente les mêmes interfaces distantes que celles de l'objet distant. Ainsi, elle permet au client d'invoquer à distance des méthodes  dont la logique métier est implémentée dans le serveur et de modifier le comportement de certains objets situés dans ce dernier.
Plus précisément, quand une méthode est appelée sur une instance de la classe proxy, cette dernière initie une connexion avec la JVM distante, lui transmet les paramètres d'appel et récupère les résultats qu'elle va renvoyer à l'appelant.

## Yanis

La classe de stub RMI à pour rôle de faire le relais entre l'espace mémoire de la JVM d’exécution cliente et l'espace mémoire de la JVM d’exécution serveur.

# Q2. Dans votre TP, avez-vous lancé le registre dans une machine virtuelle séparée ou dans celle du serveur ? Justifiez ce choix.

## Ahmed & Azzedine

Dans notre TP, nous avons lancé le registre dans la machine virtuelle du serveur,
 car ceci permet de lancer le registre automatiquement lors de l'exécution du code.

## Yanis

J'ai lancé le registre dans la même JVM que le serveur.
Il n'y a pas besoin de lancé le registre dans une JMV séparé, parce qu'il n'existe pas de raison de le faire dans notre cas.

# Q3. Expliquez comment vous avez procédé pour mettre en place la création de nouveaux patients depuis le client.

## Ahmed & Azzedine
Pour mettre en place la création de nouveaux patients :

1- On demande à l'utilisateur de saisir les informations du patient.

2- On appel via un proxy une méthode "ajouterAnimal" implémenter coté serveur dans laquel :

      - On vérifie si le patient existe auquel cas on renvoie au client le message " le patient existe déjà"

      -  Si le patient n'existe pas, on crée une instance du dossier de suivi du patient, éventuellement une instance de son espèce et une instance de cet animal.

      - Si le nombre de patients dépasse les 100, 500, ou 1000 on alerte tous les vétérinaires.

      -  On renvoie au client le message "Enregistrement effectué avec succès"

Puis, coté client en affiche à l'utilisateur le message renvoyé par le serveur.

## Yanis

Pour mettre en place la création de patient côté client, j'ai préféré passé des types par valeurs ou sérialisé.
Dans un premier temps une méthode ajouterPatient(...) appeler sur le stub cabinet prenait tout les arguments explicitement.

Puis par mon observation du fonctionnement d'Android j'ai opté pour une classe Bundle qui comme en Android permet de disposer des attributs nécessaire.
J'ai donc réduis le nombre d'argument a 1.
Si dans le futur je devais ajouter des attributs,  comme le sexe, la taille, le régime à la méthode d'ajout de patient alors elle ne changerai que très peu.

# Q4. Expliquez à quelles conditions un objet peut être passé en paramètre ou en valeur de retour du client vers le serveur ou réciproquement. Donnez des exemples issus de votre code pour illustrer.

## Ahmed & Azzedine

 Un objet peut passer en paramètre ou en valeur de retour du client vers le serveur ou réciproquement si la classe de cet objet implémente l'interface sérialisable ou bien la classe de cet objet implémente une interface qui étends l'interface Remote

Exemples :

1.

La méthode (public Espece consulterEspece()throws RemoteException;) de la classe Animal permet de renvoyer du serveur vers le client un objet de type Espece dont la classe implémente l'interface sérialisable.

2.

La  méthode (public DossierSuivi recupDossierSuivi()throws RemoteException) de la classe animal permet de renvoyer du serveur vers le client un objet de type DossierSuivi qui implémente l'interface IDossierSuivi qui étend l'interface Remote.

3.

La méthode (public void ajouterConnexion(IConnexion iConnex)throws RemoteException;) permet d'envoyer en paramètre du client vers le serveur un objet dont la classe implémente l'interface IConnexion qui étend Remote.

## Yanis

La valeur doit etre de type primitif, implémenté Serializable ou Remote + UnicastRemoteObject.

Par exemple la classe suivante peut etre traiter par des proxies.

`public interface Animal  extends Remote { ... }`

Par exemple le code suivant transmet un type primitif.

`public String getRace() { return raceAnimal; }`

Par exemple la classe suivante peut etre transmise par copie.

`public interface Espece extends Serializable { ... }`


# Q5. Expliquez le mécanisme que vous avez mis en place pour permettre que les alertes arrivent chez les différents clients...

## Ahmed & Azzedine

Le mécanisme que nous avons mis en place pour permettre que les alertes arrivent chez les différents clients et le suivant :

Nous avons créé une interface "Iconnexion" dans le projet des classes partagées contenant le prototype de la méthode "alerter", qui permet d'afficher un message aux clients du  cabinet vétérinaire quand on franchit, à la hausse ou à la baisse les seuils, de 100, 500 et 1000 patients (cette interface étend l'interface Remote)

Coté client :

Nous avons créé une classe Connexion qui étends l'interface "IConnexion" décrit si dessus.

Coté Serveur:

Dans la classe CabinetVétérinaire :

1. Nous avons ajouté une liste pour sauvegarder les connexions des clients (Références des clients connéctés)

2. Pour simuler la connexion et la déconnexion d'un vétérinaire nous avons implémenté 2 méthodes "ajouterConnexion" et "supprimerConnexion" qui ajoute et supprimes des objets connexion dans la liste décrite si dessus.

3. Nous avons aussi implémenté une méthode "alerterVeterinaires"  qui permettra d'invoquer la méthode "allerter" (décrite si dessus) de tous les objets connexion.

4. La méthode "alerterVeterinaires"  est invoqué lors de l'ajout ou de la suppression d'un patient si un des seuils spécifié dans l'énoncé est franchi à la hausse ou à la baisse.

## Yanis

J'ai proposer une solution en deux étapes.

1. La première consiste coté client a instancier une classe ConnexionImplementation() dont l'interface etends Remote. Elle spécifie la méthode alert(int) qui affiche dans la console l'entier passé en paramètre.
L'objectif de cette étape c'est bien sur d'envoyer un stub au serveur qui nous amène a introduire la second étape.

2. J'ai commencer par ajouter une liste des Connexions clientes. J'ai modifier la méthode d'ajout patient pour faire un swtich case sur la taille de ma liste de patient. Les cas sont suivant les constantes : MILESTONE_X, avec X appartenant à {TEST:5, LOW:100, MEDIUM:500, HIGH:1000}. Si une condition est vérifié elle itère sur la liste des connexions en appellant alors la méthode alert(X).

# Q6. Expliquez comment vous avez testé vos programmes.

## Ahmed & Azzedine

Nous avons testé notre programme sous éclipse comme suie :

Dans la classe ServerCabinetVeterinaire :

                   -modifier le chemin absolu vers le fichier security.policy selon le chemin de votre ordinateur (modifier la chaîne de caractère "filePolicy" de la ligne 20).

                   - modifier le chemin abosulu vers le dossier du projet du client qui contient les fichier .class  utilisés pour le code base (modifier la chaîne de caractère "classPath" de la ligne 25).

Exécuter la classe ServerCabinetVeterinaire.

Exécuter la classe clientCabinetVeterinaire.

## Yanis

* La première partie du TP, soit jusqu'au la question 3 dispose de test JUNIT comme abordée au premier semestre.
Une amélioration serait d'avoir un mock serveur/client qui est la seul partie de code non couverte par mon test JUNIT (pas de recherche ni de nécéssité absolue dans mon cas).

* La deuxième partie du TP avec la mise en place d'un `CabinetVeterinaire` ne dispose pas de test JUNIT. Des instructions écrites en dur dans le main du client et des `System.out.println(...)` sont utilisés.
* Un script bash qui exécute l'instruction en boucle `java client` avait été fait pour simuler les connexions clientes et l'ajout de 100, 500, 1000 patients au serveur.