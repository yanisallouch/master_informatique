#include "commun.h"

// lire le fichier commun.h pour la documentation et la trsucture utilisée


int jetonsEtape_init(JetonsEtape * v, int k){
  int res = 0;
  struct sJetonsUneEtape *etape = (struct sJetonsUneEtape*) v; 
  if ( pthread_mutex_init(&etape->mut,NULL) &&
pthread_cond_init(&etape->cond,NULL))
  {
  	res =-1;
  }

etape->max = k;

  
  return res;
}


int demande_jeton(JetonsEtape * v){

  int res = 0;
 struct sJetonsUneEtape *etape = (struct sJetonsUneEtape*) v;
 etape->pthread_mutex_lock(&mut);
 while(etape->nbJetonPris==etape->max)
 {
 	printf("tous les places sont pris \n");
 	etape->pthread_cond_wait(&cond , &wait);
 }
 if (etape->nbJetonPris<etape->max)
 {
 	printf("nouveu jouer entre\n");
 liberer_jeton(&etape);
 }
  return res;
}

int liberer_jeton(JetonsEtape * v){

  int res = 0;
   struct sJetonsUneEtape *etape = (struct sJetonsUneEtape*) v;
	if(etape->pthread_cond_broadcast(&etape->cond) &&
 	pthread_mutex_unlock(&etape->cond))
		res=-1;

  return res;
}

int jetonsEtape_destroy(JetonsEtape * v){

  int res = 0;
  
 if (pthread_exit())
 {
 	res = -1;
 }
 
  return res;
}


