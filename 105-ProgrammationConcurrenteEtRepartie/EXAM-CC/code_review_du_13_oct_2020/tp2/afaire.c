#include "commun.h"

// lire le fichier commun.h pour la documentation et la structure utilisée


int jetonsEtape_init(JetonsEtape * v, int k){
  int res = 0;
  
  v->max = k;

  v->nbJetonPris = 0;

  res = pthread_mutex_init(&(v->mut), NULL);

  if(res==0)
  res = pthread_cond_init(&(v->cond), NULL);
  
  
  return res;
}


int demande_jeton(JetonsEtape * v){

  int res = 0;
  
  res = pthread_mutex_lock(&(v->mut));

  if (res == 0){
	  while(v->nbJetonPris >= v->max){
	  	res = pthread_cond_wait(&(v->cond), &(v->mut));
	  }

	  if (res == 0){
	  v->nbJetonPris = v->nbJetonPris + 1;

	  res = pthread_mutex_unlock(&(v->mut));
	 }
  }
  return res;
}

int liberer_jeton(JetonsEtape * v){

  int res = 0;
  
  res = pthread_mutex_lock(&(v->mut));

  if(res == 0){
  	v->nbJetonPris = v->nbJetonPris - 1;

  	res = pthread_cond_signal(&(v->cond));

  	if (res == 0)
  	res = pthread_mutex_unlock(&(v->mut));
  }

  return res;
}

int jetonsEtape_destroy(JetonsEtape * v){

  int res = 0;
  
   res = pthread_mutex_destroy(&(v->mut));

   if (res == 0){
   	res = pthread_cond_destroy(&(v->cond));
   }
 
  return res;
}


