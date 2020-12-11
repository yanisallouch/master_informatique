#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include "calcul.h"
#include <pthread.h>

pthread_mutex_t verrou = PTHREAD_MUTEX_INITIALIZER;

typedef struct paramsFonctionThread {

	int idThread;
	int * vecteur1;
	int * vecteur2;
	int * somme;
//	paramsFonctionThread_t * args;
	// si d'autres paramètres, les ajouter ici.

} paramsFonctionThread_t;

void * produitScalaireThread (void * params){

	paramsFonctionThread_t * args = (paramsFonctionThread_t *) params;

	printf("Debut du %deme thread numero %ld\n", args->idThread, pthread_self());
	int res = 0;
	res = (args->vecteur1[args->idThread] * args->vecteur2[args->idThread] );
	printf("Multiplication du %deme thread = %d\n", args->idThread, res);
	

	// mutex
	srand (time (NULL));

	pthread_mutex_lock(&verrou);
	printf("principal : verrou obtenu\n");

	printf("Thread multiplication, somme %d + %d\n", (*args->somme), res);
	(*args->somme) += res;

	printf("principal : verrou libéré\n");
	pthread_mutex_unlock(&verrou);

	calcul(0+rand() % 10);
	
	// mutex
	

	printf("Fin du %deme thread numero %ld\n", args->idThread, pthread_self());

	pthread_exit(NULL);
}

int main(int argc, char * argv[]){


	if (argc < 2 ){
		printf("utilisation: %s  nombre_threads  \n", argv[0]);
		return 1;
	}

	int * vecteur1 = (int*) malloc(sizeof(int)*atoi(argv[1]));
	int * vecteur2 = (int*) malloc(sizeof(int)*atoi(argv[1]));
	// int * res = (int) malloc(sizeof(int)*atoi(argv[1]));
	// *res = 0;
	int * somme = (int*) malloc(sizeof(int)*atoi(argv[1]));
	*somme = 0;

	for(int j = 0; j < atoi(argv[1]); j++){
		vecteur1[j] = j;
		vecteur2[j] = j+1;
	}

	pthread_t threads[atoi(argv[1])+1];

	paramsFonctionThread_t * argsThread = (paramsFonctionThread_t*) malloc(sizeof(paramsFonctionThread_t)*atoi(argv[1]));

	for (int i = 0; i < atoi(argv[1]); i++){

		argsThread[i].idThread = i;
		argsThread[i].vecteur1 = vecteur1;
		argsThread[i].vecteur2 = vecteur2;
		argsThread[i].somme = somme;

		int retourThread = pthread_create(&threads[i], NULL, produitScalaireThread, &argsThread[i]);

		if ( retourThread != 0){
			perror("erreur creation thread");
			pthread_exit(NULL);
		}
	}

	for(int i = 0; i < atoi(argv[1]); i++){
		pthread_join(threads[i], NULL);
	}

	printf("Somme total = %d\n", (*argsThread->somme));
	return 0;
}