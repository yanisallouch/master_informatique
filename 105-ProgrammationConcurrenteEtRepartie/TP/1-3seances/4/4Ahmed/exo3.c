#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>

struct rendez_vous{

	pthread_mutex_t verrou;
	pthread_cond_t total_ateint;
}rdv;

void init(struct rendez_vous *rv){
	pthread_mutex_init(&(rv->verrou),NULL);
	pthread_cond_init(&(rv->total_ateint),NULL);

}

struct paramsFonctionThread {

  int idThread;
  int num_ordre;
  int nb_threads;
  int *cpt;

  // si d'autres paramètres, les ajouter ici.

};


void * fonctionThread (void * params){

  struct paramsFonctionThread * args = (struct paramsFonctionThread *) params;
 
  // faire un travaille
  int b=0;
  for (int i = 0; i < 100000000; ++i)
  {
  	b=b+5*9/63;
  }

  // faire vérro pour cpt
  pthread_mutex_lock(&rdv.verrou);
   *(args->cpt) = *(args->cpt)+1;
  if (*(args->cpt)==args->nb_threads)
  {
  	// réveiller
  	printf("thread num %d  reveiller tous le monde car cpt = %d \n",args->num_ordre, *(args->cpt));
  	pthread_cond_broadcast(&rdv.total_ateint);
  }else{
  	printf("thread num %d attendre car cpt = %d \n",args->num_ordre, *(args->cpt));

  	pthread_cond_wait(&rdv.total_ateint,&rdv.verrou);
  }
   pthread_mutex_unlock(&rdv.verrou);
  

}


int main(int argc, char * argv[]){

  if (argc < 2 ){
    printf("utilisation: %s  nombre_threads  \n", argv[0]);
    return 1;
  }     

  

  int *cpt;
  cpt=(int*)malloc(sizeof(int));
  *cpt=0;

  init(&rdv);

  pthread_t *threads;
  threads=(pthread_t *)malloc(atoi(argv[1])*sizeof(pthread_t));

  struct paramsFonctionThread *params;
  params =(paramsFonctionThread *)malloc(atoi(argv[1])*sizeof(paramsFonctionThread));

  
  // création des threards 
  for (int i = 0; i < atoi(argv[1]); i++){
    (params+i)->num_ordre =i;
    (params+i)->nb_threads=atoi(argv[1]);
    (params+i)->cpt=cpt;


   // compléter pour initialiser les paramètres
    if (pthread_create(&threads[i], NULL,fonctionThread,(params+i)) != 0){
      perror("erreur creation thread");
      exit(1);
    }
  }
for (int i = 0; i < atoi(argv[1]); ++i)
{
  pthread_join(threads[i],(void**) &cpt);
}




  return 0;
 
}
 
