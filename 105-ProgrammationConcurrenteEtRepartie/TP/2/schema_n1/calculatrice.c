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
		perror("Serveur erreur ftok");
		exit(1);
	}

	printf("Serveur : Clé généré avec succès :)\n");

	// la clé numérique calculée, je réupère l'identifiant de la file (ici je suppose que la file existe.

	int msgid = msgget(cle, IPC_CREAT | 0666);

	if(msgid == -1) {
		perror("Serveur erreur msgget");
		exit(1);
	}

	printf("Serveur : File de message accédé ou crée avec succès :D\n");

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

	bool flag = true;

	while(flag == true){
		printf("Serveur : En attente de message à traiter (:\n");
		if(msgrcv(msgid, (void *)&maRequete, sizeof(maRequete.contenu), 1, 0) == -1){
			perror("Serveur erreur msgrcv");
			exit(1);
		}else{
			printf("Serveur : opération reçue  => %d %c %d\n", maRequete.contenu.op1, (char) maRequete.contenu.idop, maRequete.contenu.op2);
			printf("Serveur : Message en traitement :)\n");
			int res = 0;
			switch (maRequete.contenu.idop){
				case 'm' :
					res = maRequete.contenu.op1 * maRequete.contenu.op2;
					printf("Serveur : L'opération est un '*'\n");
					break;

				case 's' :
					res = maRequete.contenu.op1 - maRequete.contenu.op2;
					printf("Serveur : L'opération est un '-'\n");
					break;

				case 'd' :
					res = maRequete.contenu.op1 / maRequete.contenu.op2;
					printf("Serveur : L'opération est un '/'\n");
					break;

				case 'a' :
					res = maRequete.contenu.op1 + maRequete.contenu.op2;
					printf("Serveur : L'opération est un '+'\n");
					break;

				default :
					flag = false;
					maRequete.contenu.flag = flag;
					printf("Serveur : Message d'arret reçu :/\n");
			}

			maRequete.contenu.res = res;
			maRequete.etiqReq = (long) 2;

			if(msgsnd(msgid, (void *)&maRequete, sizeof(maRequete.contenu), 0) == -1){
				perror("Serveur erreur msgsnd");
				exit(1);
			}
			printf("Serveur : Réponse envoyé :D\n");
		}
	}
	return 0;
}