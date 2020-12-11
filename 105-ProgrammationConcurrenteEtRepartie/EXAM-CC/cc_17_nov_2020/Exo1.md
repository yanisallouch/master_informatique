Spécifications du problème :

Dans le cadre d’une application multi-processus, on souhaite mettre en œuvre un appel de fonction asynchrone.  L'architecture de cette application à l'exécution est composée d'un processus dit "Appelant" et d'un autre processus dit "Appelé". 

Le processus "Appelé"  tourne en boucle infinie . A chaque itération, ce processus attend un appel d'une fonction "f(...)" en provenance du processus "Appelant", exécute cette fonction et répond au processus "Appelant" pour lui communiquer le résultat .

Le processus "Appelant" , effectue (pour les besoins de l'exercice), 50 fois la séquence suivante : appeler la fonction "f(....)",  effectuer un traitement "autre(....)" en parallèle avec l'exécution de "f(...)", attendre le résultat de "f(...)" et afficher ce résultat avant de passer à l'itération suivante.

Pour réponde à cet exercice, on choisit d'utiliser un tableau de sémaphores pour effectuer les communications (appels et réponses) et un segment de mémoire partagé pour y stocker le résultat d'une exécution de "f(...)" (remarque : chaque appel de "f(...)" écrasera le résultat de l'appel précédent). 

Le point de départ pour mettre en oeuvre cette application est le squelette algorithmique suivant, représentant les processus "Appelant" et "Appelé" :

Appelant

  int idSem = identifiant du tableau de sémaphores;

  int idShm = identifiant du segment de mémoire partagé
  for (int i = 1; i <= 50; i++){
    // appeler fonction f(...)
    ...   // (1)
    // faire autre chose pendant que f(...) s'exécute
    autre(...); 
    // attendre la fin de l'exécution de f(...)
    ...    // (2)

    // afficher le résultat

    afficherContenu (idShm);
  }
Fin Appelant

Appelé

  int idSem = identifiant du tableau de sémaphores;

  int idShm = identifiant du segment de mémoire partagé
  while (1){
    // attendre un appel de fonction f(...)
    ...   // (3)
    // exécuter f(...)
    résultat = f(...); 

    écrireRésultatDans (idShm, résultat);

    // répondre à l'appelant
    ...    // (4)

  }
Fin Appelé

En supposant que les objets IPC sont créés et initialisés avant le lancement des ces deux processus.

L'objectif est de compléter les zones (1), (2), (3) et (4) indiquées dans ce schéma algorithmique.

Remarque : vous devez utiliser les opérations P, V et/ou Z sur des sémaphores suivant la sémantique vue en cours et en utilisant la  syntaxe suivante :
  - P(k, Si) s'applique au sémaphore se trouvant à l'indice i du tableau, avec une soustraction de k. Exemple : P(5, S2) correspond à une opération P avec la valeur 5 à soustraire de la valeur du sémaphore positionné à l'indice 2 du tableau d'identifiant idSem. 
  - V(k, Si) s'applique au sémaphore  se trouvant à l'indice i du tableau avec une incrémentation de k. 
  - Z (Si) s'applique au sémaphore  se trouvant à l'indice i du tableau.

