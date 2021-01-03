# Aspect 1 :
Rapport :
Lire le document pdf déposé avec le code du projet , retrouvez vous les éléments suivants :

	* politique de reservation supporté par le système implémenté :

		Non, Une requête peut supporter plusieurs réservations mais on ne sait pas si on peut prendre les ressources en exclusives ou en partager.

	* architecture (les processus et threads composant l'application) :

		Oui, c'est expliqué, on apprend que le serveur à bien un thread d'initialisation et un serveur fils par client ce dernier possédant en plus 1 thread, et le client un thread de saisie et un thread d'affichage.

	* structure de l'état du systeme de reservation (si existants) :

		Oui, il est indiqué qu'il existe une structure qui permet de faire des réservations.

	* ressources :

		Oui, On a bien une structure d'ensemble de sites

	* réservations et leur mode :

		Incomplet, il manque la différenciation partagé/exclusif et leur libération.

	* objets IPC utilisés et principe de leur utilisation :

		Oui, synthétiquement ils détaillent les objets IPC suivants : sémaphore de protection et segment de mémoire partagé.

	* stratégie de synchronisation :

		Oui, point de rendez-vous indiqué côté serveur et client.

	* échanges de messages entre serveur et clients :

		Oui, tableau pour les quantités de ressources ok, un détail des échanges de messages est bien présent.

Si TOUS les éléments sont présents ET si le descriptif donne une idée claire du projet réalisé : attribuer 2 points. Si c'est moyennement complet, 1 point, sinon 0. (Remarque : il ne s'agit pas de vérifier que la solution proposée est correcte mais d'évaluer la capacité de synthèse de ce qui a été mis en place)


Remarque : si vous avez attribué des points mais qu'à l’exécution, vous constatez des incohérences, retirez les points attribués ici.

# Aspect 2 :

README et compilation : (total 2 points)

* Lire le fichier README :

Les instructions pour la compilation et le lancement de l’exécution sont elles claires ? Oui : 1 point, sinon 0.

		Oui, pas d'ambiguité pour les paramètres.
		Cependant, les deux commandes pour lancer le serveur ou le client n'est pas standard. Il faut ajouter "./" au programme serveur et clients. Il ne faut pas supposer que l'utilisateur possède le repertoire courant dans la variable d'environnement $PATH

* Compilation

Allez dans le répertoire du projet, puis compilez.

Aucune erreur ET aucun warning à la compilation : 1 points, sinon 0

		Oui, compilation correcte.

mais...
Attention, les seules erreurs tolérées à la compilation et qui sont à corriger sont celles liées à l’absence d’une librairie à inclure (erreurs du genre : fonction de la librairie standard non définie ou redéfinition qui peuvent être résolues simplement par l'ajout ou la suppression de lignes #include ...). Si c’est le cas, corriger et compiler à nouveau.

Rappel : dans la majorité des cas, les warning non pris en compte provoquent des erreurs à l’exécution.

A ce stade, si la compilation échoue, évaluer tous les aspects de tests d’exécution à 0 (puisque les tests ne sont pas faisables) et passer au derniers aspects de l'évaluation (analyse du code).

# Aspect 3

Première étape de l’exécution (mise en place du système):

Vous évaluez une application distribuée donc, vous devez utiliser des machines reliées par un réseau (vous ne devez pas faire les tests sur une seule machine). Si une connexion d'un client au serveur à distance échoue, la suite des tests n'est pas faisable en distribué (assurez vous quand même qu'il n'y a pas d'erreur d'adresse ou autre de votre part). Si c'est le cas,  vous pouvez alors évaluer le travail sur une seule machine mais le mentionner par mail à bouziane@lirmm.fr et Julie.Cailler@lirmm.fr.


Avant de lancer l'exécution et à l’aide de la commande ipcs, vérifier les objets IPC existants sur la machine du serveur.

Lancer un serveur et deux clients distants (idéalement en utilisant au total trois machines). Mentionner, en commentaire, le nombre de machines utilisées et la répartition des processus sur ces machines.

		Un serveur sur la machine A, deux clients sur deux machines distantes distinctes respectivement B-virtuel-1 et B-virtuel-2. On observe une mémoire partagée créer côté serveur.

		Pour résumer, côté serveur sur la machine A : on a le serveur principal qui a pour pid 161, deux serveurs fils, respectivement pid 166 et pid 169 avec une mémoire partagée. Côté client, on observe la création d'un sémaphore par client.

C’est parti  !


Chaque client visualise/affiche t-il une copie de l’état initial des ressources disponibles (état du système de réservation) sans avoir à le demander ? Oui : 1 point, sinon 0.

		Oui, pour une première exécution, cependant, dès qu'il se produit un bug, le client n'affiche ni les réservations ni les sites connus. Par exemple pour reproduire le bug : lancer le serveur sur la machine A (voir capture 1), sur la machine B-v1 exécuter un client, il devrait afficher les sites et les réservations du client (voir capture 1-1). Sur B-v2 exécuter un second client qui devrait fonctionner (voir capture 1-2) sinon on observe le résultat suivant : avec aucun affichage, des sites ou des réservations ne s'effectuent pas. (voir capture 1-3)

Avec la commande ipcs, vérifier qu’un et seulement un segment de mémoire partagé a bien été créé sur la machine du serveur. Si oui, 1 point, sinon 0.

		Oui. objets IPCS bien créées.

Toujours avec la commande ipcs, vérifier qu’un, deux ou trois (pas plus !) tableaux de sémaphores ont bien été créé sur la machine du serveur, si oui, 1 point, sinon 0.

		1 tableau de sémaphore crée côté serveur, et 1 sémaphore par client (capture 1-1 et 1-2).

Attention : si aucun objet IPC n’est créé par l’application, cela signifie que le travail effectué est hors sujet. Arrêter l’évaluation à ce niveau en mettant 0 à tous les aspects de tests d'exécution suivants (pour passer à l'analyse du code).
Faites pareil si un client (à condition qu'il s'exécute sur une machine différente de celle du serveur) a créé des objets IPC (il n'en a pas besoin puisqu'il doit être multithread). Laisser aussi un commentaire en Feedback général


# Aspect  4 & 5 & 6 & 7 & 8

		Ne sont pas évaluer parce que le client a crée une semaphore (machine distincte du serveur) => note ==>0


# Aspect 9

Un peu de lecture.

Lire plusieurs bouts de code et vérifiez les points suivants :

- Le code est facile à lire et commenté :

		Oui, facile a lire, commenter proprement.

- Le nom des variables et des fonctions sont significatifs (ont un sens facilitant la compréhension de leur  :

		Oui, rien de particulier à signaler.

- Le code est bien structuré (indentation, etc.) et absence de code inutile en commentaire :

		Oui, indentation, pas de code inutile.

Si TOUT est bon : 1 point, sinon 0.

Vérifier ce qui suit :

- toutes les ressources offertes par le système doivent être représentées dans un tableau de sémaphore. Vérifier l’existence d'un tableau de sémaphores dans lequel ont retrouve pour chaque chaque site deux sémaphores : un pour les CPU et un pour l'espace de stockage. Ce même tableau doit inclure aussi un sémaphore représentant le verrou pour protéger l'accès au segment de mémoire partagé. Sans un tableau regroupant ces sémaphores, une réservation atomique ne peut être mise en place correctement. :

		Non, le rapport est clair et le code va dans ce sens, il n'y a pas la structure indiqué.

- Pour réserver plusieurs ressources, un ensemble d'opération sur les sémaphores sont exécutés en un seul appel à semop(..) :

		Vu qu'il n'y a pas la structure attendue, en effet il n'y a pas d'appel a semop pour la réservation de plusieurs ressources.

- Il n'y a pas d'attente active. :

		Il n'y a pas d'attente active.

- La fonction sleep () n’est jamais utilisée (vous pouvez le vérifier avec la commande grep) :

		La fonction sleep() n'est jamais utilisée. La seule mention de sleep apparaît dans un fichier de test qui n'est pas utilisé par le client ni le serveur.

- Les fonctions utilisées pour la manipulation des objets IPC et la programmation mutiltheads sont celles vues en cours (pas d’utilisation d’autres libraires standards) :

	Oui, les fonctions pour la manipulation des objets IPC et multithread sont celles vues en cours.

- Le code ne semble pas être du plagiat :

	Oui, le code ne semble pas être du plagiat.

Si au moins un point parmi ces 4 n'est pas respecté, laisser  un commentaire dans le feedback général. Faire de même si la majorité du code semble être du plagiat ou hors contexte (ne changez pas votre évaluation. Nous prendrons en compte ces problèmes). Signaler aussi la situation par mail à bouziane@lirmm.fr et Julie.Cailler@lirmm.fr.


Feedback général :

Les Aspect 4 & 5 & 6 & 7 & 8 :

		* Ne sont pas évalués parce que le client a créé un sémaphore (machine distincte du serveur) => noté ==> 0 aux aspects correspondants.

		C'est dommage de noter si faiblement un tel code ! J'ai trouvé les commentaires pertinents, la structuration du code est excellente cependant vous avez peut-être un peu trop découpé dans différents fichiers, mon binôme et moi avons eu quelques difficultés à la première lecture pour repérer certaines implémentations de fonctions.

		Par exemple, vous implémentez la fonction manage_user dans un fichier d'en-tête (.hpp) ce qui n'est pas intuitif vu votre structuration.

		Pour revenir sur le rapport, on le trouve assez court. Il aurait été préférable détaillé avec plus d'explication et d'image votre travail.

		On aurait souhaité pouvoir effectuer les tests d’exécution...


Total : 7/33
