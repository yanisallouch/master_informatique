#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>  // <cstdbool> en C++

int main (int argc, char * argv[]){

	if(argc != 5){
		printf("lancement : ./client operande1 operande2 operation chemin_fichier_cle\n");
		exit(1);
	}

// file used for the key : ./ipc

	// récuperer l'identifiant de la file de message qu'on souhaite
	// utiliser. La clé est une paire : chemin vers un fichier existant
	// et un caractère (ou entier en fonction de l'OS). La même paire
	// permet d'identifier une seule file de message. Donc tous les
	// processus qui utiliseront la même paire, partageront la même
	// file de message (s'ils en ont les droits aussi)

	key_t cle = ftok(argv[4], 436);

	if (cle == -1) {
		perror("Client erreur ftok");
		exit(1);
	}

	printf("Client : Clé généré avec succès :)\n");

	// la clé numérique calculée, je réupère l'identifiant de la file (ici je suppose que la file existe.

	int msgid = msgget(cle, IPC_CREAT | 0666);

	if(msgid == -1) {
		perror("Client erreur msgget");
		exit(1);
	}

	printf("Client : File de message accédé ou crée avec succès :D\n");

	// structure des requetes
	typedef struct req{
		long etiqReq; // 1
		struct contenu{
			int idop;  // 1: +, 2 : -, 3: *, 4: /
			int op1;
			int op2;
			int res;
			bool flag;
		} contenu;
	} requete;

	requete maRequete;

	// initialiser un message avant de l'envoyer.
	maRequete.etiqReq = 1;
	maRequete.contenu.op1 = atoi(argv[1]);
	maRequete.contenu.op2 = atoi(argv[2]);
	maRequete.contenu.idop = *argv[3];
	maRequete.contenu.res = 0;
	maRequete.contenu.flag = true;
	// envoi requete
	if(msgsnd(msgid, (void *)&maRequete, sizeof(maRequete.contenu), 0) == -1){
		perror("Client erreur msgsnd");
		exit(1);
	}else{
		printf("Client : opération envoyé => %d %c %d\n", maRequete.contenu.op1, (char) maRequete.contenu.idop, maRequete.contenu.op2);
		if(msgrcv(msgid, (void *)&maRequete, sizeof(maRequete.contenu), (long) 2, 0) == -1){
			perror("Client erreur msgrcv");
			exit(1);
		}else{
			if(maRequete.contenu.flag == true){
				printf("Client : Résultat is %d\n", maRequete.contenu.res);
			}else {
				printf("Client : Une demande sans opérande à été envoyé, le serveur à confirmé son arrêt :/\n");
			}
		}
	}
	return 0;
}