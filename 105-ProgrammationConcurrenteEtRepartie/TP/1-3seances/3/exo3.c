#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include "calcul.h"
#include <pthread.h>


typedef struct dCommune {
	int *task;
	pthread_mutex_t *verrou;
	pthread_cond_t *condi;
	int nTask;
	int ind;
} buffer_t ;

void * fnt_task(void * parametres){
//task 1
	buffer_t *param = (buffer_t*) parametres;

	int n = rand() % 10;
	printf("Debut 1st task du %deme thread avec %ds de calcul\n", param->ind, n);
	calcul(0+n);

	//section critique 
	pthread_mutex_lock((param->verrou));

	*(param->task) = *(param->task) + 1;
	
	if(*(param->task) >= param->nTask){
		printf("%deme thread voit ses copains au loin ! \n", param->ind);
		pthread_cond_broadcast((param->condi));
	}else{
		while(*(param->task) < (param->nTask)){
			printf("%deme thread en attente de ses copains ! \n", param->ind);
			pthread_cond_wait((param->condi),(param->verrou));
			printf("%deme thread a retrouver ses amis ! \n", param->ind);
		}
	}
	pthread_mutex_unlock((param->verrou));
	//section critique 
	
//task 2
	printf("Debut 2nd task du %deme thread \n", param->ind);

	srand (time (NULL));
	calcul(0+rand() % 10);
	printf("Fin du %deme thread \n", param->ind);
	
	pthread_exit(NULL);
}

int main(int argc, char * argv[]){
	srand (time (NULL));

	if (argc < 2 ){
		printf("utilisation: %s  nombre_threads  \n", argv[0]);
		return 1;
	}

	pthread_mutex_t verrou = PTHREAD_MUTEX_INITIALIZER;
	pthread_cond_t condi = PTHREAD_COND_INITIALIZER;

	int task = 0;
	buffer_t * data = (buffer_t*) malloc (sizeof(buffer_t)*atoi(argv[1]));

	pthread_t threads[atoi(argv[1])+1];

	for (int i = 0; i < atoi(argv[1]); i++){

		data[i].task = &task;
		data[i].nTask = atoi(argv[1]);
		data[i].ind = i;
		data[i].verrou = &verrou;
		data[i].condi = &condi;

		int retourThread = pthread_create(&threads[i], NULL, fnt_task, &data[i]);

		if ( retourThread != 0){
			perror("erreur creation thread");
			pthread_exit(NULL);
		}
	}
	for(int i = 0; i < atoi(argv[1]); i++){
		pthread_join(threads[i], NULL);
	}
	
	printf("Fin du programme \n");

	return 0;
}