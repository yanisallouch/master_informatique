#include <stdlib.h>
#include <time.h>    // time()
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>  // <cstdbool> en C++


void * decomposition(void * structure){
	printf("Je suis enfant n°%d\n", getpid());
	
	return NULL;
}

int main (int argc, char * argv[]){

	if(argc != 3){
		printf("lancement : ./decomposition path_file nbEnfants\n");
		exit(1);
	}

	if(atoi(argv[2]) < 1){
		printf("please enter a number of childs above 0\n");
	}

// file used for the key : ./ipc

	// récuperer l'identifiant de la file de message qu'on souhaite
	// utiliser. La clé est une paire : chemin vers un fichier existant
	// et un caractère (ou entier en fonction de l'OS). La même paire
	// permet d'identifier une seule file de message. Donc tous les
	// processus qui utiliseront la même paire, partageront la même
	// file de message (s'ils en ont les droits aussi)

	key_t cle = ftok(argv[1], 436);

	if (cle == -1) {
		perror("Parent erreur ftok");
		exit(1);
	}

	printf("Parent : Clé généré avec succès :)\n");

	// la clé numérique calculée, je réupère l'identifiant de la file (ici je suppose que la file existe.

	int msgid = msgget(cle, IPC_CREAT | 0666);

	if(msgid == -1) {
		perror("Parent erreur msgget");
		exit(1);
	}

	printf("Parent : File de message accédé ou crée avec succès :D\n");

	// structure des requetes
	typedef struct message {
		long etiqReq; // 0 || 1
		struct contenu{
			int n;
			int facteur1;
			int facteur2;
			int facteurSeul;
			int *messagesEnfant;
		} contenu;
	} requete;

	requete maRequete;

	// initialiser un message avant de l'envoyer.
	maRequete.etiqReq = 0; // 0 signifie premier depot.

	printf("Parent PID %d\n",getpid() );

	srand(time(NULL));
	
	maRequete.etiqReq = 1;
	maRequete.contenu.n = rand() % 255;
	maRequete.contenu.messagesEnfant = (int*) malloc(sizeof(int)*atoi(argv[2]));
	for(int i = 0; i < atoi(argv[2]); i++){
		maRequete.contenu.messagesEnfant[i] = 0;		 
	}
	maRequete.contenu.facteur1 = 0;
	maRequete.contenu.facteur2 = 0;
	maRequete.contenu.facteurSeul = 0;
	
	size_t * pid = (size_t*) malloc(sizeof(int)*atoi(argv[2]));

	// envoi requete
	if(msgsnd(msgid, (void *)&maRequete, sizeof(maRequete.contenu), 0) == -1){
		perror("Parent erreur msgsnd");
		exit(1);
	}else{
		printf("Parent : création de %d enfant\n", atoi(argv[2]) );
		
		size_t parent = getpid();

		for (int i = 0; i < atoi(argv[2]) && parent == getpid() ; i++){
			pid[i] = fork();
			// Je fais le travail de décomposition que si je suis l'enfant.
			if (pid[i] == 0){
				decomposition(&maRequete);
			}
		}

//		for ( int i = 0; i < 2; i++){ printf("PID  : %d\n", getpid()); }

		// if(pid != 0){
		// 	if(msgrcv(msgid, (void *)&maRequete, sizeof(maRequete.contenu), (long) 2, 0) == -1){
		// 		perror("Parent erreur msgrcv");
		// 		exit(1);
		// 	}else{
		// 	}
		// }else{
		// }
	}
	return 0;
}