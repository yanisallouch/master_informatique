# Aspect 1 :
Rapport :
Lire le document pdf déposé avec le code du projet , retrouvez vous les éléments suivants :

* politique de reservation supporté par le système implémenté :

		oui, on précise bien les réservations supportées.

* architecture (les processus et threads composant l'application) :

		Oui, on apprend même qu'un serveur fils possède un thread de mise à jour et un thread de requête.
		Le client à un thread de mise à jour et un de sasie/affichage.

* structure de l'état du systeme de reservation (si existants) :

		oui, on indique bien comment est structuré une reservation et le systeme.

* ressources :

		oui.

* réservations et leur mode :

		Dans une requête, on indique bien la ville, suivie des quantités demandées en CPU et stockage, ainsi que le mode (partagé/exclusif) et le type (prise /libération)

* objets IPC utilisés et principe de leur utilisation :

		oui, il y a toute une section.

* stratégie de synchronisation :

		On a bien explicité.

* échanges de messages entre serveur et clients :

		oui.

Si TOUS les éléments sont présents ET si le descriptif donne une idée claire du projet réalisé : attribuer 2 points. Si c'est moyennement complet, 1 point, sinon 0. (Remarque : il ne s'agit pas de vérifier que la solution proposée est correcte mais d'évaluer la capacité de synthèse de ce qui a été mis en place)


Remarque : si vous avez attribué des points mais qu'à l’exécution, vous constatez des incohérences, retirez les points attribués ici.

# Aspect 2 :

README et compilation : (total 2 points)

* Lire le fichier README :

Les instructions pour la compilation et le lancement de l’exécution sont elles claires ? Oui : 1 point, sinon 0.

		oui.

* Compilation

Allez dans le répertoire du projet, puis compilez.

Aucune erreur ET aucun warning à la compilation : 1 points, sinon 0

		Aucune erreur, par contre on compile avec -Wall -Wextra -pedantic et on a laissé la même variable non utilisée 3 fois pour la gestion du signal. (SIGINT et SIGQUIT) côté serveur et SIGINT côté client. Ce n'est vraiment pas un warning à prendre en compte vu les flags activer.

mais...
Attention, les seules erreurs tolérées à la compilation et qui sont à corriger sont celles liées à l’absence d’une librairie à inclure (erreurs du genre : fonction de la librairie standard non définie ou redéfinition qui peuvent être résolues simplement par l'ajout ou la suppression de lignes #include ...). Si c’est le cas, corriger et compiler à nouveau.

Rappel : dans la majorité des cas, les warning non pris en compte provoquent des erreurs à l’exécution.

A ce stade, si la compilation échoue, évaluer tous les aspects de tests d’exécution à 0 (puisque les tests ne sont pas faisables) et passer au derniers aspects de l'évaluation (analyse du code).

# Aspect 3

Première étape de l’exécution (mise en place du système):

Vous évaluez une application distribuée donc, vous devez utiliser des machines reliées par un réseau (vous ne devez pas faire les tests sur une seule machine). Si une connexion d'un client au serveur à distance échoue, la suite des tests n'est pas faisable en distribué (assurez vous quand même qu'il n'y a pas d'erreur d'adresse ou autre de votre part). Si c'est le cas,  vous pouvez alors évaluer le travail sur une seule machine mais le mentionner par mail à bouziane@lirmm.fr et Julie.Cailler@lirmm.fr.


Avant de lancer l'exécution et à l’aide de la commande ipcs, vérifier les objets IPC existants sur la machine du serveur.

Lancer un serveur et deux clients distants (idéalement en utilisant au total trois machines). Mentionner, en commentaire, le nombre de machines utilisées et la répartition des processus sur ces machines.



C’est parti  !


Chaque client visualise/affiche t-il une copie de l’état initial des ressources disponibles (état du système de réservation) sans avoir à le demander ? Oui : 1 point, sinon 0.

		non

Avec la commande ipcs, vérifier qu’un et seulement un segment de mémoire partagé a bien été créé sur la machine du serveur. Si oui, 1 point, sinon 0.

		oui

Toujours avec la commande ipcs, vérifier qu’un, deux ou trois (pas plus !) tableaux de sémaphores ont bien été créé sur la machine du serveur, si oui, 1 point, sinon 0.

		oui

Attention : si aucun objet IPC n’est créé par l’application, cela signifie que le travail effectué est hors sujet. Arrêter l’évaluation à ce niveau en mettant 0 à tous les aspects de tests d'exécution suivants (pour passer à l'analyse du code).
Faites pareil si un client (à condition qu'il s'exécute sur une machine différente de celle du serveur) a créé des objets IPC (il n'en a pas besoin puisqu'il doit être multithread). Laisser aussi un commentaire en Feedback général


# Aspect  4 & 5 & 6 & 7 & 8

		Les aspects, ne sont pas évalués parce que le client a créé un mémoire partagé (machine distincte du serveur) => noté ==> 0


# Aspect 9

Un peu de lecture.

Lire plusieurs bouts de code et vérifiez les points suivants :

- Le code est facile à lire et commenté :

		Oui, le code est facile à lire, mais commenté de façon inconsistante.

- Le nom des variables et des fonctions sont significatifs (ont un sens facilitant la compréhension de leur  :

		oui

- Le code est bien structuré (indentation, etc.) et absence de code inutile en commentaire :

		Oui, il est bien structuré, mais présence de codes inutile en commentaire.

Si TOUT est bon : 1 point, sinon 0.

Vérifier ce qui suit :

- toutes les ressources offertes par le système doivent être représentées dans un tableau de sémaphore. Vérifier l’existence d'un tableau de sémaphores dans lequel ont retrouve pour chaque chaque site deux sémaphores : un pour les CPU et un pour l'espace de stockage. Ce même tableau doit inclure aussi un sémaphore représentant le verrou pour protéger l'accès au segment de mémoire partagé. Sans un tableau regroupant ces sémaphores, une réservation atomique ne peut être mise en place correctement. :

		Il existe un seul tableau de sémaphore pour protéger l'accès en mémoire partagée d'un site.

- Pour réserver plusieurs ressources, un ensemble d'opération sur les sémaphores sont exécutés en un seul appel à semop(..) :

		oui.

- Il n'y a pas d'attente active. :

		Il y a de l'attente active.

- La fonction sleep () n’est jamais utilisée (vous pouvez le vérifier avec la commande grep) :

		la fonction sleep est utilisée à deux reprises.

- Les fonctions utilisées pour la manipulation des objets IPC et la programmation mutiltheads sont celles vues en cours (pas d’utilisation d’autres libraires standards) :

		Oui, ce sont celles vue en cours.

- Le code ne semble pas être du plagiat :

		Non, ce n'est pas du plagiat.

Si au moins un point parmi ces 4 n'est pas respecté, laisser  un commentaire dans le feedback général. Faire de même si la majorité du code semble être du plagiat ou hors contexte (ne changez pas votre évaluation. Nous prendrons en compte ces problèmes). Signaler aussi la situation par mail à bouziane@lirmm.fr et Julie.Cailler@lirmm.fr.


Feedback général :

Les Aspect 4 & 5 & 6 & 7 & 8 :

* Les aspects ne sont pas évalués parce que le client a créé une mémoire partagé (machine distincte du serveur) => noté ==> 0 aux aspects correspondants.

		Le code possède une coquille sur le port qui avait été hardcodé côté serveur. Du commenté inutile.
		On retrouve des bugs à l'exécution qui ne sont pas désirés si les objets IPCS ou TCP ne sont pas proprement nettoyés. Le rapport est très bien détaillé, la lecture est assez rapide. Cependant avec un peu plus de temps et des attentes bien mieux définis dès le départ et des informations non-contradictoires, nous n'aurions pas raté plusieurs critères d'évaluation.

Total : 7/33
