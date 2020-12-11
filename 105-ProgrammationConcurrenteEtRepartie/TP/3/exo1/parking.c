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

	if(argc != 3){
		printf("lancement : ./parking nbPlaces ipc\n");
		exit(1);
	}

// file used for the key : ./ipc

	// récuperer l'identifiant de la file de message qu'on souhaite
	// utiliser. La clé est une paire : chemin vers un fichier existant
	// et un caractère (ou entier en fonction de l'OS). La même paire
	// permet d'identifier une seule file de message. Donc tous les
	// processus qui utiliseront la même paire, partageront la même
	// file de message (s'ils en ont les droits aussi)

	key_t cle = ftok(argv[2], 634);

	if (cle == -1) {
		perror("Parking erreur ftok");
		exit(1);
	}

	printf("Parking : Clé généré avec succès :)\n");

	int idSem = semget(cle, 1, IPC_CREAT | 0666);
	if(idSem == -1) {
		perror("Parking erreur semget");
		exit(1);
	}

	printf("Parking : Semaphore accédé ou crée avec succès :D\n");

	struct parking {
		int nbPlacesUtilisee;
		int nbPlacesMax;
	};

	int shId = shmget(cle, (1*sizeof(struct parking*)), IPC_CREAT|0666);

	if(shId == -1){
		perror("Parking erreur shmget");
		exit(1);
	}

	printf("Parking : Parking partagé en mémoire, accédé ou crée avec succès :D\n");

	struct parking * addrShmParking = shmat(shId, NULL, 0);

	if((void*) addrShmParking == (void*)-1){
		perror("Parking erreur shmat");
		exit(1);
	}

	addrShmParking->nbPlacesUtilisee = 0;
	addrShmParking->nbPlacesMax = atoi(argv[1]);

	printf("Parking : %d place max initialisé\n", addrShmParking->nbPlacesMax);

	union semun egCtrl;

	egCtrl.val=1;
	if(semctl(idSem, 0, SETVAL, egCtrl) == -1){
		perror("Parking problème init");
		exit(1);
	}

	printf("Parking : Ensemble de Semaphore initialisé\n");

	return 0;
}