# Question 1 :

2 sémaphores (ce n'est pas de l'exclusion mutuelle !) le premier sémaphore gère la fonction le temps qu'elle envoie ces résultats
le deuxième bloque la fonction qui attend d'être appeler et permet justement à la première d'être utiliser en parallèle (d'ou les 2 sémaphores)

Donc 2 sémaphores, l'un init à 0 et l'autre à 1.

# Pour la dernière question : Il n'y avait pas d'erreur dans la solution proposé.

# Pour la question ou un processus demande la suppression :

- Tableau de sémaphore et file de message erreur quand on utilise
- Segment de mémoire partagé, le système le détruit pas forcément immédiatement (il atteint que le nombre d'attachement atteigne 0)

# Question sur exclusion mutuelle :

-Verrou au sens mutex et conditionnelle
- Files de messages
-Segment de mémoire
-Tableau de sémaphores
-Verrou au sens mutex