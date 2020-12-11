#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include "calcul.h"
#include <pthread.h>

typedef struct dCommune {
	int *zones;
	pthread_mutex_t * verrou;
	pthread_cond_t * condi;
	int nbZoneMax;
	int indice;
} buffer_t ;

void * fnt_zone(void * parametres){
	buffer_t *param = (buffer_t*) parametres;
	int retourThread = 0;
	for(int i = 0; i < param->nbZoneMax; i++){
		int n = 1;
		n += rand() % 8;
		printf("Thread %d - valeur zone %d : %d ",param->indice, i, *((param->zones) + i));
		if((param->indice)  == *((param->zones) + i)){
			printf("j'ai du travail !\n");
		}else{
			printf("je n'ai pas de travail !\n");
		}
		if( (param->indice)  == *((param->zones) + i) ){
		/**
		*	si l'indice inscrit dans la (zone[0+ i]) référent au thread
		*	qui est passer par la avant est == (au thread accesseur -1)
		*	alors il peut travailler
		*/
			//section critique
			printf("Debut du travail du %deme thread avec %ds de calcul sur la zone %d\n", param->indice, n, i);

			retourThread = pthread_mutex_lock((param->verrou));
			if ( retourThread != 0){
				perror("Erreur obtention verrou\n");
				pthread_exit(NULL);
			}
			calcul(n);
			*((param->zones) + i)  = param->indice + 1;
			//j'indique la zone comme disponible pour le traitement (courant + 1)
			retourThread = pthread_mutex_unlock((param->verrou));
			if ( retourThread != 0){
				perror("Erreur liberation verrou\n");
				pthread_exit(NULL);
			}
			//section critique
			retourThread = pthread_cond_broadcast(param->condi);
			if ( retourThread != 0){
				perror("Erreur broadcast verrou\n");
				pthread_exit(NULL);
			}
		}else{
		/**
		*	sinon il attend qu'il se fasse reveiller
		*/
			i--; // pourquoi ? voir la trace d'execution
			retourThread = pthread_mutex_lock((param->verrou));
			if ( retourThread != 0){
				perror("Erreur obtention verrou\n");
				pthread_exit(NULL);
			}
			while (1) {
				retourThread = pthread_cond_wait(param->condi,param->verrou);
				if ( retourThread != 0){
					perror("Erreur cond wait verrou\n");
					pthread_exit(NULL);
				}
				break;
			}
			retourThread = pthread_mutex_unlock((param->verrou));
			if ( retourThread != 0){
				perror("Erreur liberation verrou\n");
				pthread_exit(NULL);
			}
		}
	}
	printf("Fin du %deme thread \n", param->indice);
	pthread_exit(0);
}




int main(int argc, char * argv[]){
	srand (time (NULL));

	// if (argc < 2 ){
	// 	printf("utilisation: %s  nombre_threads  \n", argv[0]);
	// 	return 1;
	// }

	int nbTravail = 10;
	int nbZoneMax = 25;
	int * zones = malloc(sizeof(int)*nbZoneMax);
	for (int i = 0; i < nbZoneMax; ++i){
		zones[i] = 0;
	}

	printf("Zones initialiser a %d !\n", zones[0]);

	pthread_mutex_t verrou = PTHREAD_MUTEX_INITIALIZER;
	pthread_cond_t condi = PTHREAD_COND_INITIALIZER;

	buffer_t * data = (buffer_t*) malloc (sizeof(buffer_t)*2);
	// 2 travail different pour l'instant

	pthread_t threads[10];
	// alors 2 threads differents

	printf("Thread a initialiser !\n");
	int retourThread = 0;
	for (int i = 0; i < nbTravail; i++){

		data[i].zones = zones;
		data[i].verrou = &verrou;
		data[i].condi = &condi;
		data[i].nbZoneMax = nbZoneMax;
		data[i].indice = i;

		retourThread = pthread_create(&threads[i], NULL, fnt_zone , &data[i]);
		printf("Thread %d - crée\n", i);
		if ( retourThread != 0){
			perror("Erreur creation thread\n");
			pthread_exit(NULL);
		}
	}

	void * returnValue = NULL;
	for(int i = 0; i < nbTravail; i++){
		retourThread = pthread_join(threads[i],returnValue);
	}
	printf("Fin du programme \n");

	return 0;
}
