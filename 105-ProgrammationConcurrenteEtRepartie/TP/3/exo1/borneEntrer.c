#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/sem.h>
#include <sys/shm.h>
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>  // <cstdbool> en C++

union semun {
	int              val;    /* Valeur pour SETVAL */
	struct semid_ds *buf;    /* Tampon pour IPC_STAT, IPC_SET */
	unsigned short  *array;  /* Tableau pour GETALL, SETALL */
	struct seminfo  *__buf;  /* Tampon pour IPC_INFO (spécifique à Linux) */
};

int main (int argc, char * argv[]){

	srand(time(NULL));

	if(argc != 2){
		printf("lancement : ./borneEntrer ipc\n");
		exit(1);
	}

// file used for the key : ./ipc

	// récuperer l'identifiant de la file de message qu'on souhaite
	// utiliser. La clé est une paire : chemin vers un fichier existant
	// et un caractère (ou entier en fonction de l'OS). La même paire
	// permet d'identifier une seule file de message. Donc tous les
	// processus qui utiliseront la même paire, partageront la même
	// file de message (s'ils en ont les droits aussi)

	key_t cle = ftok(argv[1], 634);

	if (cle == -1) {
		perror("BorneEntrer erreur ftok");
		exit(1);
	}

	printf("BorneEntrer : Clé généré avec succès :)\n");

	// la clé numérique calculée, je réupère l'identifiant de la file (ici je suppose que la file existe.

	int idSem = semget(cle, 1, IPC_CREAT | 0666);

	if(idSem == -1) {
		perror("BorneEntrer erreur semget");
		exit(1);
	}

	printf("BorneEntrer : Semaphore accédé ou crée avec succès :D\n");

	int shId = shmget(cle, (1*sizeof(int*)), IPC_CREAT|0666);

	if(shId == -1){
		perror("BorneEntrer erreur shmget");
		exit(1);
	}

	printf("BorneEntrer : Mémoire partagé accédé ou crée avec succès :D\n");

	struct parking {
		int nbPlacesUtilisee;
		int nbPlacesMax;
	};

	struct parking * addrShm = (struct parking *) shmat(shId, NULL, 0);

	if((void*)addrShm == (void*)-1){
		perror("BorneEntrer erreur shmat");
		exit(1);
	}

	// struct sembuf {
	// 	unsigned short	sem_num ; 	/* Numéro du sémaphore */
	// 	short	sem_op ;	 Opération sur le sémaphore
	// 	short	sem_flg ;	/* Options par exemple SEM UNDO */
	// };

	struct sembuf * opp = (struct sembuf *) malloc(sizeof(struct sembuf));

	opp->sem_num=0;
	opp->sem_op=-1;
	opp->sem_flg=SEM_UNDO;

	struct sembuf opv = (struct sembuf *) malloc(sizeof(struct sembuf));

	opv.sem_num=0;
	opv.sem_op=+1;
	opv.sem_flg=SEM_UNDO;

	while(true){

		sleep(rand() % 5);
		int res = semop(idSem,opp,1);

		if(res == -1){
			perror("BorneEntrer erreur semop");
		}
		printf("------------------------ BorneEntrer : Semaphore verrouille\n");

// faire entrer des voitures

		if(addrShm->nbPlacesUtilisee < addrShm->nbPlacesMax){
			printf("BorneEntrer : Demande accepté, il y a %d place disponible\n", addrShm->nbPlacesMax - addrShm->nbPlacesUtilisee); // afficher place
			addrShm->nbPlacesUtilisee += 1;
			printf("BorneEntrer : Il reste %d place après votre entrer\n",addrShm->nbPlacesMax - addrShm->nbPlacesUtilisee);
		}else{
			printf("BorneEntrer : Parking plein, il est necessaire d'attendre qu'une place ce libere\n");
		}

		res = semop(idSem,&opv,1);

		if(res == -1){
			perror("BorneEntrer erreur semop");
		}
		printf("------------------------ BorneEntrer : Semaphore deverrouille\n");

		sleep(rand() % 10);

	}

	return 0;
}