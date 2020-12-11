Si dans une application multi-processus, un processus Proc_d demande à supprimer un object IPC obj, une erreur peut se produire pendant l'exécution d'un autre processus Proc_a, si : 
Veuillez choisir au moins une réponse :

obj est un tableau de sémaphores et Proc_a est en attente que la valeur d'un sémaphore de ce tableau soit égale à 0 en même temps que le traitement de la destruction.

obj est un tableau de sémaphores et Proc_a est en train d'écrire dans un segment de mémoire partagé protégé par un sémaphore de ce tableau, ceci en même temps que le traitement de la destruction.

obj est un segment de mémoire partagé et Proc_a est en train d'écrire ou lire dans ce segment en même temps que le traitement de la destruction.

obj est une file de messages et Proc_a est en train d'attendre sur l'extraction d'un message de la file en même temps que le traitement de la destruction.
