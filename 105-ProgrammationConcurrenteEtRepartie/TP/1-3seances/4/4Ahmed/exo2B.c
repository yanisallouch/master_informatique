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
	pthread_t numOrdre;
	int valeur1;
	int valeur2;
	int *somme;
};

pthread_mutex_t mutex_stdout = PTHREAD_MUTEX_INITIALIZER;

void * function_thread(void *params)
{
	struct paramsFonctionThread * p = (struct paramsFonctionThread *) params;
	pthread_mutex_lock(&mutex_stdout);
	printf("je suis le thread %ld la première valeur reçu est %d le seconde est %d\n",p->numOrdre,p->valeur1,p->valeur2 );
	sleep(1);
	pthread_mutex_unlock(&mutex_stdout);
    *(p->somme)=*(p->somme) +(p->valeur1 * p->valeur2);
    pthread_mutex_lock(&mutex_stdout);
    printf("je suis le thread %ld la valeur de la somme après mon execution est %d\n",p->numOrdre,*(p->somme));
    sleep(1);
    pthread_mutex_unlock(&mutex_stdout);
    free(params);
	pthread_exit(0);

}
int main(int argc, char * argv[]){

  if (argc < 2 ){
    printf("utilisation: %s  nombre_threads  \n", argv[0]);
    return 0;
  }  
  int vect1 [atoi(argv[1])] ;
  int vect2 [atoi(argv[1])] ;
  int *res;
  res=(int*)malloc(sizeof(int));
  *res=0; 
 // int res=0;
  int *ret;
  ret=(int*)malloc(sizeof(int));
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
}
for (int i = 0; i < atoi(argv[1]); ++i)
{
	params=(paramsFonctionThread*) malloc(sizeof(paramsFonctionThread));
	params->numOrdre=i;
	params->valeur1=vect1[i];
	params->valeur2=vect2[i];
	params->somme=res;
	pthread_create(&threads[i],NULL,function_thread,params);
}
for (int i = 0; i < atoi(argv[1]); ++i)
{
	pthread_join(threads[i],(void**)ret);
}

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
printf("\nle produit scalaire est %d\n",*res );
/*int sp=0;
for (int i = 0; i < atoi(argv[1]); ++i)
{
	sp+=vect2[i]*vect1[i] ;
	printf(" prod =%d ",vect2[i]*vect1[i] );
	printf("somme =%d\n",sp );
}
*/
	return 0;
}