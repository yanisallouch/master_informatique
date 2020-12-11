#include "commun.h"


int jetonsEtape_init(JetonsEtape * v, int k){
  int res = 0;
  v->max=k;
  v->nbJetonPris=0;
  if(pthread_mutex_init(&(v->mut),NULL) != 0)
    {
      res=-1;
    }

  if(pthread_cond_init(&(v->cond),NULL) != 0)
    {
      res=-1;
    }
  
  return res;
}


int demande_jeton(JetonsEtape * v){

  int res = 0;
  if(pthread_mutex_lock(&(v->mut))!=0)
    {
      res=-1;
      return res;
    }
  while((v->nbJetonPris)+1>v->max)
    {
      if(pthread_cond_wait(&(v->cond),&(v->mut))!=0)
	{
	  res=-1;
	  return res;
	}
    }
  (v->nbJetonPris)++;
  if(pthread_mutex_unlock(&(v->mut))!=0)
    {
      res=-1;
      return res;
    }
 
  return res;
}

int liberer_jeton(JetonsEtape * v){

  int res = 0;
  if(pthread_mutex_lock(&(v->mut))!=0)
    {
      res=-1;
      return res;
    }
  (v->nbJetonPris)--;
  if(pthread_mutex_unlock(&(v->mut))!=0)
    {
      res=-1;
      return res;
    }
  if(pthread_cond_broadcast(&(v->cond))!=0)
    {
      res=-1;
      return res;
    }
    

  return res;
}

int jetonsEtape_destroy(JetonsEtape * v){

  int res = 0;
  return res;
}


