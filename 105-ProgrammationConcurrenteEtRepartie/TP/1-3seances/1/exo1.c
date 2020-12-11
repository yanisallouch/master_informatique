#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include "calcul.h"

typedef struct paramsFonctionThread {

	int idThread;

	// si d'autres paramètres, les ajouter ici.

} paramsFonctionThread_t;

void * fonctionThread (void * params){

	paramsFonctionThread_t * tmp = (paramsFonctionThread_t *) params;

	//  struct paramsFonctionThread * args = (struct paramsFonctionThread *) params;
	printf("Debut thread numero : %ld et mon pid : %d et le %deme\n", pthread_self(), getpid(), tmp->idThread);
	calcul(5);
	printf("Fin thread numero : %ld et mon pid : %d et le %deme\n", pthread_self(), getpid(), tmp->idThread);
	//pthread_exit(NULL);
	// a compléter
	return 0;
}

int main(int argc, char * argv[]){

	if (argc < 2 ){
		printf("utilisation: %s  nombre_threads  \n", argv[0]);
		return 1;
	}

	pthread_t threads[atoi(argv[1])];

	paramsFonctionThread_t * numero = (paramsFonctionThread_t*) malloc(sizeof(paramsFonctionThread_t)*1);
	
	// création des threards
	for (int i = 0; i < atoi(argv[1]); i++){

		numero->idThread = i;
		int retourThread = pthread_create(&threads[i], NULL, fonctionThread, numero);

		numero = (paramsFonctionThread_t*) malloc(sizeof(paramsFonctionThread_t)*1);

		if ( retourThread != 0){
			perror("erreur creation thread");
			pthread_exit(NULL);
		}
	}

	// garder cette saisie et modifier le code en temps venu.
	char c = 'c';
	// printf("saisir un caractere \n");
	fgets(&c, 2, stdin);
	// printf("%c \n", c);

	return 0;
}