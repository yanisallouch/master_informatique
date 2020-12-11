#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>
#include <time.h>

struct paramsFonctionThread
{
	pthread_t num_ordre;
	int ind;
	int *tab1;
	int *tab2;
	int *prod;
};

void * function_thread(void *params)
{
	struct paramsFonctionThread * p = (struct paramsFonctionThread *) params;
	printf("je suis le thread %ld la première valeur reçu est %d le seconde est %d\n",p->num_ordre,p->tab1[p->ind],p->tab2[p->ind]);
	p->prod[p->ind]= p->tab1[p->ind] * p->tab2[p->ind];
	printf("je suis le thread %ld le produit retourné = %d\n",p->num_ordre,p->prod[p->ind]);
	free(p);
	pthread_exit(0);

}
int main(int argc, char * argv[]){

  if (argc < 2 ){
    printf("utilisation: %s  nombre_threads  \n", argv[0]);
    return 0;
  }  
  int vect1 [atoi(argv[1])] ;
  int vect2 [atoi(argv[1])] ;
  int prod [atoi(argv[1])] ;

  int *ret;
  int resultat=0;
pthread_t *threads;
threads = (pthread_t*) malloc((atoi(argv[1]))*sizeof(pthread_t));

struct paramsFonctionThread *params;
//params=(paramsFonctionThread*) malloc((atoi(argv[1]))*sizeof(paramsFonctionThread));
srand(time(NULL));

// initialisation

for(int i=0;i<atoi(argv[1]);i++)
{	
	vect1[i]=rand() % 100;
	vect2[i]=rand() % 100;
	prod[i]=0;
}
//création des threads

for (int i = 0; i < atoi(argv[1]); ++i)
{	params=(paramsFonctionThread*) malloc(sizeof(paramsFonctionThread));
	params->num_ordre =i;
	params->ind=i;
	params->tab1=vect1;
	params->tab2=vect2;
	params->prod=prod;
	pthread_create(&threads[i],NULL,function_thread,params);
}

//attente de la fin des threads

for (int i = 0; i < atoi(argv[1]); ++i)
{
	pthread_join(threads[i],(void**)&ret);
}

//calcule de la somme 
for (int i = 0; i < atoi(argv[1]); ++i)
{
	resultat=resultat+prod[i];
}
//affichage
printf("Tableau 1 \n");
for (int i = 0; i < atoi(argv[1]); ++i)
{
	printf("%d \t",vect1[i] );
}
printf("\nTableau 2 \n");
for (int i = 0; i < atoi(argv[1]); ++i)
{
	printf("%d \t",vect2[i] );
}
printf("\nle produit scalaire est %d\n",resultat );
	return 0;
}