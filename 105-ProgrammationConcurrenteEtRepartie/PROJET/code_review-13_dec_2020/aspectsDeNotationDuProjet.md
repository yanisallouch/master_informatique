# Aspect 1

Rapport :

 Lire le document pdf déposé avec le code du projet :

Retrouvez vous les éléments suivants : politique de réservation supporté par le système implémenté, architecture (les processus et threads composant l’application), structure de l'état du système de réservation (sites (si existants), ressources, réservations et leur mode), objets IPC utilisés et principe de leur utilisation, stratégie de synchronisation, échanges de messages entre serveur et clients.

Si TOUS les éléments sont présents ET si le descriptif donne une idée claire du projet réalisé : attribuer 2 points. Si c'est moyennement complet, 1 point, sinon 0. (Remarque : il ne s'agit pas de vérifier que la solution proposée est correcte mais d'évaluer la capacité de synthèse de ce qui a été mis en place)


Remarque : si vous avez attribué des points mais qu'à l’exécution, vous constatez des incohérences, retirez les points attribués ici.
Note pour Aspect 1 : /2
Commentaire pour Aspect 1

# Aspect 2

README et compilation : (total 2 points)

* Lire le fichier README :

Les instructions pour la compilation et le lancement de l’exécution sont elles claires ? Oui : 1 point, sinon 0.

Remarque : si vous avez attribué des points mais qu'à l’exécution, vous constatez que les instructions étaient incomplètes , retirez les points attribués ici.


 * Compilation

Allez dans le répertoire du projet, puis compilez.

Aucune erreur ET aucun warning à la compilation : 1 points, sinon 0,  mais...

Attention, les seules erreurs tolérées à la compilation et qui sont à corriger sont celles liées à l’absence d’une librairie à inclure (erreurs du genre : fonction de la librairie standard non définie ou redéfinition qui peuvent être résolues simplement par l'ajout ou la suppression de lignes #include ...). Si c’est le cas, corriger et compiler à nouveau.


Rappel : dans la majorité des cas, les warning non pris en compte provoquent des erreurs à l’exécution.


A ce stade, si la compilation échoue, évaluer tous les aspects de tests d’exécution à 0 (puisque les tests ne sont pas faisables) et passer au derniers aspects de l'évaluation (analyse du code).



Note pour Aspect 2 : /2
Commentaire pour Aspect 2

# Aspect 3

Première étape de l’exécution (mise en place du système):

Vous évaluez une application distribuée donc, vous devez utiliser des machines reliées par un réseau (vous ne devez pas faire les tests sur une seule machine). Si une connexion d'un client au serveur à distance échoue, la suite des tests n'est pas faisable en distribué (assurez vous quand même qu'il n'y a pas d'erreur d'adresse ou autre de votre part). Si c'est le cas,  vous pouvez alors évaluer le travail sur une seule machine mais le mentionner par mail à bouziane@lirmm.fr et Julie.Cailler@lirmm.fr.


Avant de lancer l'exécution et à l’aide de la commande ipcs, vérifier les objets IPC existants sur la machine du serveur.

Lancer un serveur et deux clients distants (idéalement en utilisant au total trois machines). Mentionner, en commentaire, le nombre de machines utilisées et la répartition des processus sur ces machines.

C’est parti  !


Chaque client visualise/affiche t-il une copie de l’état initial des ressources disponibles (état du système de réservation) sans avoir à le demander ? Oui : 1 point, sinon 0.

Avec la commande ipcs, vérifier qu’un et seulement un segment de mémoire partagé a bien été créé sur la machine du serveur. Si oui, 1 point, sinon 0.

Toujours avec la commande ipcs, vérifier qu’un, deux ou trois (pas plus !) tableaux de sémaphores ont bien été créé sur la machine du serveur, si oui, 1 point, sinon 0.


Attention : si aucun objet IPC n’est créé par l’application, cela signifie que le travail effectué est hors sujet. Arrêter l’évaluation à ce niveau en mettant 0 à tous les aspects de tests d'exécution suivants (pour passer à l'analyse du code). Faites pareil si un client (à condition qu'il s'exécute sur une machine différente de celle du serveur) a créé des objets IPC (il n'en a pas besoin puisqu'il doit être multithread). Laisser aussi un commentaire en Feedback général


Note pour Aspect 3 : /3
Commentaire pour Aspect 3

# Aspect 4

Premiers tests de réservations  / synchronisation : réservations exclusives sur un site :

Dans cet aspect, vous allez commencer par demander des ressources disponibles, pour demander ensuite des ressources non disponibles. Le tout est à faire sur un même site offrant au moins 3 CPU  (s'il n'y a pas de notion de sites, cela fonctionnera aussi). Choisissez de réserver des CPU.

Poursuivre l’exécution en réalisant ce qui suit :

Sur un client : effectuer une simple réservation : exemple 1 CPU en mode exclusif. Cette réservation doit être satisfaite (il y a assez de CPU). Si ce n'est pas le cas, aucun autre test n'aboutira, donc attribuer 0 à cet aspect et aux aspects tests suivants avant de passer à l'analyse du code.

Après cette demande, avez vous l’information que le CPU a bien été réservé ? Si oui 1 point, sinon 0.

La diffusion du nouvel état du système sur chaque client a t-elle été effectuée ? en d'autre termes, l’affichage est-il mis à jour AUTOMATIQUEMENT chez LES DEUX clients (il se passe quelque chose chez tous les clients)?  Oui : 1 point, sinon 0.

Si le résultat de la  mise à jour est identique chez les deux clients et s'il correspond bien au nouvel état du système de réservation) : 1 point, sinon 0.

Sur le second client : demander un nombre X de CPU, où X = 1 + nombre de CPU qui sont encore libres. Le client est-il bloqué en attente que sa demande soit satisfaite ? Oui : 1 point, sinon 0. Attention : ici, l'état du système ne doit pas changer : il y a toujours un seul CPU réservé et tout le reste est libre. Si ce n'est pas le cas, retirer le point.

Lancez un troisième client (sur une nouvelle machine ou sur la machine du serveur ou d'un autre client): demander cette fois un nombre de CPU disponibles (un nombre < X ). Si cette demande est satisfaite et si l'état du système est mis à jour correctement chez tous les clients : 1 point, sinon 0.


Revenir sur le premier client : libérer le CPU réservé.  Si l'état du système est mis à jour correctement chez tous les clients et que le second client reste toujours bloqué : 1 point, sinon 0. Attention : ici, le nouvel état du système doit indiquer un CPU de plus de libre par rapport à l'état avant la libération et en aucun cas attribuer le CPU au client en attente. Si ce n'est pas le cas, retirer le point.

Revenir sur le troisième client : libérer les CPU réservés. Si le second client est réveillé et si l'état du système est mis à jour correctement chez tous les clients : 1 point, sinon 0.

Revenir sur le second client et libérer les CPU pris. Terminer ensuite l'exécution des clients. Si vous avez pu terminer proprement les clients (sans avoir à tuer les processus via des raccourcis clavier ou une commande de type Kill ) 1 point, sinon 0.

Arrêter le serveur et supprimer les objets IPC.


Note pour Aspect 4 : /8
Commentaire pour Aspect 4

# Aspect 5

Dans cet aspect, vous allez tester la possibilité de réserver une combinaison de ressources (toutes les réservations de cet aspect sont à faire en mode exclusif).

Lancer l’application avec 3 clients (toujours en mettant en commentaire le nombre de machines utilisées et la répartition des processus sur ces machines):

Sur un premier client, réserver des CPU + un espace de stockage (sur un seul site). Vu que c'est le premier client, la demande doit être satisfaite (vous devez respecter le nombre total de ressources offertes par le système). Si la réservation a bien été effectuée, 1 point, sinon 0. (pas de test de la diffusion du nouvel état car fait à l'aspect 4, vérifiez donc bien que vous n'avez pas attribué des points à l'aspect 4 si la diffusion n'a pas eu lieu).

Sur le second client, faire une demande de réservation d'un certain nombre de CPU + un espace de stockage sur un autre site (demande qui doit aussi être satisfaite). Si la réservation a bien été effectuée, 1 point, sinon 0.

Sur le troisième client : faire une réservation incluant des CPU et un espace de stockage sur les deux sites choisis par les deux premiers clients. Cette réservation doit inclure des ressources disponibles et non disponibles (donc, une demande qui ne peut être satisfaite). Si le client est mis en attente (bloqué) ET que l'état du système ne change pas sur l'ensemble des client (rien n'a été attribué à ce troisième client) 1 point, sinon 0. Rappel : les demandes sont atomiques : on obtient tout ce qu'on demande ou rien).

Lancer un quatrième client et faire une demande de réservation de ressources qui peut être satisfaite, en incluant des ressources de l'un des sites demandés par le premier ou deuxième client. Si la réservation a bien été effectuée (pas de blocage), 1 point, sinon 0.

Libérer toutes les ressources réservées par les trois clients. Le troisième client (qui était bloqué) est-il réveillé avec une demande de réservation enfin satisfaite ? Si oui 1 point, sinon 0.


Si TOUT s’est bien passé jusqu'à présent, tester plusieurs situations similaires. S'assurer que l’exécution se poursuit correctement et sans problème (posez vous les mêmes questions précédentes), Si tout se passe bien jusqu'au bout : 1 point, sinon 0.

(Rappel : Toujours supprimer les objets IPC si vous relancer l'exécution de l'application).


Note pour Aspect 5 : /6
Commentaire pour Aspect 5

# Aspect 6

Tests de mise en œuvre des réservations partagées.

Relancer l'exécution avec 3 clients (après avoir supprimé les objets IPC).

Sur un premier client, réserver toutes les ressources d'un site en mode partagé. Cette réservation doit évidement réussir sans blocage.

Sur le second client, réserver, toujours en mode partagé, une partie des ressources du même site choisi par le premier client. Si la réservation est satisfaite sans attente, 1 point, sinon 0.

Sur le troisième client, réserver des ressources en mode exclusif. La réservation doit inclure des ressources du site choisi par le premier client. Si le client est mis en attente (bloqué) 1 point, sinon 0.

Libérer les ressources du premier et deuxième client. Si le troisième client est réveillé et que sa demande est désormais satisfaite, 1 point, sinon 0.


Si un client (l'un des trois ou en lançant un nouveau), faire une réservation de la moitié des ressources d'un site en mode partagé. Si un second client demande aussi la moitié des ressources du même site en mode partagé, dans le nouvel état du système, la moitié des ressources doit toujours rester disponible. Si c'est bien le cas 1 point, sinon 0.

Si un autre client demande la moitié des ressources du même site en mode exclusif (faire le test bien sûr), sa demande doit être satisfaite. Aussi, dans le nouvel état du système, il doit rester 0 ressources disponibles sur le site (sauf en mode partagé). Si c'est bien le cas, 1 point, sinon 0.

Si l'exécution mentionne une limite du nombre de réservations partagées avant de piocher dans les ressources libres pour une demande en mode partagé, tester cette limite. Si tout se passe bien : 1 point, sinon 0.


Note pour Aspect 6 : /6
Commentaire pour Aspect 6

# Aspect 7

A ce stade, vous devez avoir compris les fonctionnalités attendues du système de réservation. Évaluer cet aspect si tout s'est bien passé jusqu'ici, sinon, attribuer 0 et passer à la suite.

 Effectuer des tests variés avec un nombre de client supérieur à 10.  Si tout se passe aussi bien, 1 point, sinon 0.

  Si des fonctionnalités en plus ont été mises en oeuvre, testez les (en plus veut dire que tout ce qui était nécessaire a bien été mis en œuvre et qu'ici vous testez des Bonus). Attribuer 1 point si tout s'est bien passé, sinon 0.  Pensez à décrire ces fonctionnalités bonus en feedback.

Note pour Aspect 7 : /2
Commentaire pour Aspect 7

# Aspect 8

Gestion des départs de clients et des erreurs.

Cet aspect est valable si un minimum d’actions est faisable sur les clients sans problème à l’exécution.

Les tests de cet aspect demandent de relancer l’exécution. Le faire avec au moins 3 clients et supprimer tous les objets IPC entre chaque exécution.

Lancer l'exécution avec 3 clients.

Effectuer une réservation par un premier client, puis une autre par un second client.  Tester le départ d’un des deux clients (un client doit pouvoir demander à quitter et se terminer). En poursuivant l'exécution des deux autres clients, l’exécution se poursuit-elle correctement ? Oui : 1 point, sinon 0.


Gestion des erreurs :

Test 1 :

Relancer l'exécution avec deux clients (toujours supprimer les objets IPC avant).

 Pendant l'exécution, sur la machine du serveur, supprimer un tableau de sémaphores à l’aide de la commande « ipcrm -s » en passant en argument l'identifiant du tableau (ipcs). Puis poursuivre ou essayer de poursuivre l’exécution.

Si les clients s’arrêtent proprement, 1 point, sinon 0. S’arrêter proprement signifie : absence d’inter-blocage, pas de boucles infinies, pas d’arrêt sans gestion d’erreur (exemple : si le message d’arrêt est « segmentation fault », cela signifie qu’une erreur n’a pas été traitée)).


Test 2 :

Relancer l'exécution avec deux clients (toujours supprimer les objets IPC avant).

Tuer un processus client (touches CTRL-C ou commande « kill -9 » ou variante, en passant en argument l’identifiant du processus). Observez ce qui se produit et/ou tenter de faire de nouvelles réservations ou libérations de ressources depuis les autres clients.

L’exécution se poursuit-elle sans problèmes (la disparition d’un processus ne doit pas affecter la suite de l’exécution) ? Oui : 1 point, sinon 0.
Note pour Aspect 8 : /3
Commentaire pour Aspect 8

# Aspect 9

Un peu de lecture.

Lire plusieurs bouts de code et vérifiez les points suivants :

- Le code est facile à lire et commenté,

- Le nom des variables et des fonctions sont significatifs (ont un sens facilitant la compréhension de leur rôle),

- Le code est bien structuré (indentation, etc.) et absence de code inutile en commentaire,

Si TOUT est bon : 1 point, sinon 0.

Vérifier ce qui suit :

- toutes les ressources offertes par le système doivent être représentées dans un tableau de sémaphore. Vérifier l’existence d'un tableau de sémaphores dans lequel ont retrouve pour chaque chaque site deux sémaphores : un pour les CPU et un pour l'espace de stockage. Ce même tableau doit inclure aussi un sémaphore représentant le verrou pour protéger l'accès au segment de mémoire partagé. Sans un tableau regroupant ces sémaphores, une réservation atomique ne peut être mise en place correctement.

- Pour réserver plusieurs ressources, un ensemble d'opération sur les sémaphores sont exécutés en un seul appel à semop(..).

- Il n'y a pas d'attente active.  

- La fonction sleep () n’est jamais utilisée (vous pouvez le vérifier avec la commande grep)

- Les fonctions utilisées pour la manipulation des objets IPC et la programmation mutiltheads sont celles vues en cours (pas d’utilisation d’autres libraires standards)

- Le code ne semble pas être du plagiat

Si au moins un point parmi ces 4 n'est pas respecté, laisser  un commentaire dans le feedback général. Faire de même si la majorité du code semble être du plagiat ou hors contexte (ne changez pas votre évaluation. Nous prendrons en compte ces problèmes). Signaler aussi la situation par mail à bouziane@lirmm.fr et Julie.Cailler@lirmm.fr.



Note pour Aspect 9 : /1
Commentaire pour Aspect 9
Feedback général
Feedback pour l'auteur



TOTAL : /36
