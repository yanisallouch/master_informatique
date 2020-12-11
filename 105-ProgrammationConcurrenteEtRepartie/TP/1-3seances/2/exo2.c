#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include "calcul.h"
#include <pthread.h>



typedef struct paramsFonctionThread {

	int idThread;
	int * vecteur1;
	int * vecteur2;
	int res;
	int * somme;
	// si d'autres paramètres, les ajouter ici.

} paramsFonctionThread_t;






void * produitScalaireThread (void * params){
	
	paramsFonctionThread_t * args = (paramsFonctionThread_t *) params;
	args->res +=  (args->vecteur1[args->idThread] * args->vecteur2[args->idThread] );

	int * resRet = malloc(sizeof(int)); 
	*resRet = args->res;

	printf("Debut du %deme thread numero %ld\n", args->idThread, pthread_self());
	calcul(1);
	printf("Multiplication du %deme thread = %d\n", args->idThread, args->res);
	printf("Fin du %deme thread numero %ld\n", args->idThread, pthread_self());
	
	pthread_exit(resRet);
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

	pthread_t threads[atoi(argv[1])];

	paramsFonctionThread_t * argsThread = (paramsFonctionThread_t*) malloc(sizeof(paramsFonctionThread_t)*atoi(argv[1]));

	for (int i = 0; i < atoi(argv[1]); i++){

		argsThread[i].idThread = i;
		argsThread[i].vecteur1 = vecteur1;
		argsThread[i].vecteur2 = vecteur2;
		argsThread[i].res = 0;
		argsThread[i].somme = somme;

		int retourThread = pthread_create(&threads[i], NULL, produitScalaireThread, &argsThread[i]);

		if ( retourThread != 0){
			perror("erreur creation thread");
			pthread_exit(NULL);
		}

	}

	int resGlob = 0;

	for(int i = 0; i < atoi(argv[1]); i++){
		int * resTmp = malloc(sizeof(int));
		pthread_join(threads[i], (void**)&resTmp);
		printf("Thread principal fait la somme de %d + %d\n", (*argsThread->somme), *resTmp);
		(*argsThread->somme) += *resTmp; 
		resGlob += *resTmp;
	}

	printf("Thread principal affiche la somme total de %d\n", resGlob);

	return 0;
}