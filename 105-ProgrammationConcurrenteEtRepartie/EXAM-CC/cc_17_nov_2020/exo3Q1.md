Cette solution répond t-elle à la spécification de l'application ? Pour répondre à cette question, cocher dans ce qui suit toute affirmation correcte.
Veuillez choisir au moins une réponse :

Le code de ces threads répond bien à la spécification et il n'y a pas d'erreur.

Il y a une erreur, l'attente dans thread serveur et le thread client (pthread_cond_wait(...) ) doit être effectuée dans une boucle while. 

Il y a d'autres erreurs qui ne sont pas mentionnées dans les propositions de réponses

Il y a une erreur : il faut une seule variable conditionnelle et non deux.

Le thread serveur contient un erreur : il doit garder le verrou pour modifier la date (appel mettreAjour(&date)) car cette dernière est partagée en lecture et écriture.
Le thread client contient une erreur : il faut libérer le verrou avant ou après l'appel " pthread_cond_signal(&cond1);" et reprendre la verrou avant if(...)

