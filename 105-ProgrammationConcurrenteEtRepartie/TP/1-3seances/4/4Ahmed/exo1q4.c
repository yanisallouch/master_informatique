#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>


struct paramsFonctionThread {

  int num_ordre;
  int *val;

  // si d'autres paramètres, les ajouter ici.

};

pthread_mutex_t mu_Stdout = PTHREAD_MUTEX_INITIALIZER;
void * fonctionThread (void * params){

  struct paramsFonctionThread * p= (struct paramsFonctionThread *) params;
  pthread_mutex_lock(&mu_Stdout);
  printf("je suis le thread numéro %d , la valeur reçu est %d \n",p->num_ordre,*(p->val));
  sleep(2);
  pthread_mutex_unlock(&mu_Stdout);
  *(p->val)=*(p->val)+1;
  pthread_mutex_lock(&mu_Stdout);
  printf("je suis le thread numéro %d , la valeur envoyé est %d \n",p->num_ordre,*(p->val));
  sleep(2);
  pthread_mutex_unlock(&mu_Stdout);
  pthread_exit(p->val);
  free(p);

}


int main(int argc, char * argv[]){

  if (argc < 2 ){
    printf("utilisation: %s  nombre_threads  \n", argv[0]);
    return 1;
  }     

  int *var;
  var=(int*)malloc(sizeof(int));
  pthread_t *threads;
  threads=(pthread_t *)malloc(atoi(argv[1])*sizeof(pthread_t));
  struct paramsFonctionThread *params;
 // params =(paramsFonctionThread *)malloc(atoi(argv[1])*sizeof(paramsFonctionThread));


 *var=0;
  printf("je suis le programme principale la valeur initial de var est %d\n",*var );
  // création des threards 
  for (int i = 0; i < atoi(argv[1]); i++){
    params = (paramsFonctionThread *) malloc(sizeof(paramsFonctionThread));
    (params)->num_ordre =i;
    (params)->val=var;

   // compléter pour initialiser les paramètres
    if (pthread_create(&threads[i], NULL,fonctionThread,params) != 0){
      perror("erreur creation thread");
      exit(1);
    }
  }
for (int i = 0; i < atoi(argv[1]); ++i)
{
  pthread_join(threads[i],(void**)&var);
}
  printf("je suis le programme principale la valeur final de var est %d\n",*var );




  return 0;
 
}
 
