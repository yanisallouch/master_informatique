#include "commun.h"
/*
// lire le fichier commun.h pour la documentation et la trsucture utilisée




pthread_mutex_t vStdOut= PTHREAD_MUTEX_INITIALIZER;
int jetonsEtape_init(JetonsEtape * v, int k){
  int res=0;
        v->max = k;
        v->nbJetonsParis = 0;
    
       if(pthread_mutex_init((v->mut),NULL) ){
    return -1;
  }
  if(pthread_cond_init((v->cond),NULL) ){
    return -1;
  }
  return res;

}


int demande_jeton(JetonsEtape * v){


       pthread_mutex_lock(&v->mut);
        printf("Zone (%d/%d)  . \n", v->nbJetonParis, v->max );
        while(v->nbJetonParis >= v->max) {
                printf("Waiting .. \n");
                pthread_cond_wait(&v->cond, &v->mut);
        }

        printf("Zone (%d/%d) condition is OK. le joueur entre dans la zone \n", v->nbJetonParis, v->max );
        v->nbJetonParis++;

        return pthread_mutex_unlock(&v->mut);
  
 
}

int liberer_jeton(JetonsEtape * v){

   pthread_mutex_lock(&v->mut);
        v->nbJetonParis--;
        printf("A player is quiting a zone (%d/%d) . \n", v->nbJetonParis, v->max );
        if ( v->nbJetonParis < v->max) {
                pthread_cond_broadcast(&v->cond);
                printf("Zone (%d/%d) is sending a broadcast . \n", v->nbJetonParis, v->max );
        }
        return pthread_mutex_unlock(&v->mut);
}


  

int jetonsEtape_destroy(JetonsEtape * v){
  v->m = 0;
        v->nbJoueurParis = 0;
        free(&v->mut);
        return pthread_cond_destroy(&v->cond);

}

*/


