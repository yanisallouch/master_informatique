#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>  // <cstdbool> en C++

int main (int argc, char * argv[]){

	if(argc != 2){
		printf("lancement : ./calculatrice chemin_fichier_cle\n");
		exit(1);
	}

// file used for the key : ./ipc

	// récuperer l'identifiant de la file de message qu'on souhaite
	// utiliser. La clé est une paire : chemin vers un fichier existant
	// et un caractère (ou entier en fonction de l'OS). La même paire
	// permet d'identifier une seule file de message. Donc tous les
	// processus qui utiliseront la même paire, partageront la même
	// file de message (s'ils en ont les droits aussi)

	key_t cle = ftok(argv[1], 436);

	if (cle ==-1) {
		perror("Serveur ADD  erreur ftok");
		exit(1);
	}

	printf("Serveur ADD  : Clé généré avec succès :)\n");

	// la clé numérique calculée, je réupère l'identifiant de la file (ici je suppose que la file existe.

	int msgid = msgget(cle, IPC_CREAT | 0666);

	if(msgid == -1) {
		perror("Serveur ADD  erreur msgget");
		exit(1);
	}

	printf("Serveur ADD  : File de message accédé ou crée avec succès :D\n");

	typedef struct req{
		long etiqReq; // 1 | 2
		struct contenu{
			int idop;  // 1: +, 2 : -, 3: *, 4: /
			int op1;
			int op2;
			int res;
			bool flag;
		} contenu;
	} requete;

	requete maRequete;

	bool flag = true;

	while(flag == true){
		printf("Serveur ADD  : En attente de message à traiter (:\n");
		if(msgrcv(msgid, (void *)&maRequete, sizeof(maRequete.contenu), '+', 0) == -1){
			perror("Serveur ADD  erreur msgrcv");
			exit(1);
		}else{
			printf("Serveur ADD : opération reçue  => %d %c %d\n", maRequete.contenu.op1, (char) maRequete.etiqReq, maRequete.contenu.op2);
			printf("Serveur ADD  : Message en traitement :)\n");
			int res = 0;
			res = maRequete.contenu.op1 + maRequete.contenu.op2;
			maRequete.contenu.res = res;
			maRequete.etiqReq = (long) 5;

			if(maRequete.contenu.flag == false){
				flag = false;
				printf("Serveur ADD  : Message d'arret reçu :/\n");
			}

			maRequete.contenu.res = res;
			maRequete.etiqReq = (long) 2;

			if(msgsnd(msgid, (void *)&maRequete, sizeof(maRequete.contenu), 0) == -1){
				perror("Serveur ADD  erreur msgsnd");
				exit(1);
			}
			printf("Serveur ADD  : Réponse envoyé :D\n");
		}
	}
	printf("Serveur ADD : Je termine ...\n");
	return 0;
}