#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>

struct strTrdv{

	pthread_mutex_t verrou;
	pthread_cond_t total_ateint;
  int *cpt;
};

typedef struct strTrdv tRdv;

void init(tRdv *rdv){
	pthread_mutex_init(&(rdv->verrou),NULL);
	pthread_cond_init(&(rdv->total_ateint),NULL);
  rdv->cpt=(int*)malloc(sizeof(int));
  *(rdv->cpt) =0;

}

struct paramsFonctionThread {

  int idThread;
  int num_ordre;
  int nb_threads;
  tRdv *rdv;

  // si d'autres paramètres, les ajouter ici.

};


void * fonctionThread (void * params){

  struct paramsFonctionThread * p = (struct paramsFonctionThread *) params;
 
  // faire un travaille
  int b=0;
  for (int i = 0; i < 100000000; ++i)
  {
  	b=b+5*9/63;
  }

  // faire vérro pour cpt
  pthread_mutex_lock(&(p->rdv->verrou));
   
   *(p->rdv->cpt)=*(p->rdv->cpt)+1;
  if (*(p->rdv->cpt)==p->nb_threads)
  {
  	// réveiller
  	printf("thread num %d  reveiller tous le monde car cpt = %d \n",p->num_ordre,*(p->rdv->cpt));
  	pthread_cond_broadcast(&(p->rdv->total_ateint));
  }else{
  	printf("thread num %d attendre car cpt = %d \n",p->num_ordre, *(p->rdv->cpt));

  	pthread_cond_wait(&(p->rdv->total_ateint),&(p->rdv->verrou));
  }
   pthread_mutex_unlock(&(p->rdv->verrou));
   free(p);
   pthread_exit(0);
  

}


int main(int argc, char * argv[]){

  if (argc < 2 ){
    printf("utilisation: %s  nombre_threads  \n", argv[0]);
    return 1;
  }     

  int *ret;
  ret=(int*)malloc(sizeof(int));

  tRdv rdv;
  init(&rdv);



  pthread_t *threads;
  threads=(pthread_t *)malloc(atoi(argv[1])*sizeof(pthread_t));

  struct paramsFonctionThread *params;


  
  // création des threards 
  for (int i = 0; i < atoi(argv[1]); i++){

    params =(paramsFonctionThread *)malloc(sizeof(paramsFonctionThread));
    params->num_ordre =i;
    params->nb_threads=atoi(argv[1]);
    params->rdv=&rdv;

   // compléter pour initialiser les paramètres
    if (pthread_create(&threads[i], NULL,fonctionThread,params) != 0){
      perror("erreur creation thread");
      exit(1);
    }
  }
for (int i = 0; i < atoi(argv[1]); ++i)
{
  pthread_join(threads[i],(void**) &ret);
}




  return 0;
 
}
 
