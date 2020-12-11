Dans le cadre d’une application multithread, on souhaite mettre en œuvre une architecture client/serveur dans laquelle, 

- Un thread serveur attend une demande de la date et l'heure par un thread client, une fois la demande faite, met à jour une chaîne de caractères en y stockant la date et l'heure courante, puis se remet en attente d'une nouvelle demande (boucle infinie).

- Un thread client tourne en boucle (infinie) en attendant une demande de la date et l'heure par l'utilisateur, transmet cette demande au thread serveur et affiche le résultat.

On suppose que l'application est constituée d'un seul serveur et d'un seul client. Le code suivant, propose une solution mettant en oeuvre cette application. On suppose que toutes les variables sont correctement initialisées par un thread principal (non illustré ici).

 char * date;
 int saisieFaite, dateAjour; 
 pthread_mutex_t verrou;
 pthread_cond_t cond1, cond2;

 
 void * serveur (void * p){
  while (1){
    pthread_mutex_lock(&verrou);
    
    if (saisieFaite == 0) {
      pthread_cond_wait(&cond1, &verrou);
    }
    saisieFaite = 0;
    pthread_mutex_unlock(&verrou);
    mettreAjour(&date); // fonction supposée existante
    pthread_mutex_lock(&verrou);
    dateAjour = 1;
    pthread_cond_signal(&cond2);
    pthread_mutex_unlock(&verrou);
  }
  pthread_exit(NULL); 
 }
 void * client (void * p){
  char saisie
  while (1){
    saisir(&saisie); // demande une saisie au clavier
    pthread_mutex_lock(&verrou);
    saisieFaite = 1;
    pthread_cond_signal(&cond1);
    if (dateAjour == 0) {
      pthread_cond_wait(&cond2, &verrou);
    }
    dateAjour = 0;
    pthread_mutex_unlock(&verrou);
  }
  pthread_exit(NULL); 
}

Cette solution répond t-elle à la spécification de l'application ? Pour y répondre dans ce qui suit, cocher une affirmation  si elle est correcte.
