#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/ioctl.h>

struct ressources {
	char label[255];
	int nbProcesseurs;//les ressources libres
	int nbCapaciteStockage;//les ressources libres
	int nbProcesseursPartage;
	int nbCapaciteStockagePartage;
};


struct paramThreadType1 {
	int * descripteurSocket;
};

int col,row;
void get_win_value(int *col, int *row){
  struct winsize        window;

  ioctl(0, TIOCGWINSZ, &window);
/* aussi
   ioctl(0, TIOCGSIZE, &win);
   C'est la meme chose
*/
  *col = window.ws_col;
  *row = window.ws_row;
}

void affichageRessources(struct ressources * ressourcesDispo, int taille){
	system("clear");
	printf("%-5s %-15s %-11s %-11s\n", "ID", "Label", "Nb-CPU", "Stockage (Go)" );
	printf("-------------------------------------------\n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		printf("%-5d %-15s %-11d %-11d\n", (int)i, ressourcesDispo[i].label, ressourcesDispo[i].nbProcesseurs, ressourcesDispo[i].nbCapaciteStockage );
	}
	printf("-------------------------------------------\n");
}

void *fnt_miseAjourDisporessources(void * param){
	struct paramThreadType1 *paramThread = (struct paramThreadType1 *) param;
	int taille;
	// protocole :
	// recevoir la taille (taille) totale des sites disponibles
	// recevoir les donnees (ressourcesDispo) des ressources disponibles en integralite
	// tant qu'il est connecter, recevoir le nombre de modifications (tailleIndModif) a faire, puis pour chacunne, son indice (indCourant) et sa nouvelle valeur (ressourcesDispo[indCourant]);
	if(recv(*(paramThread->descripteurSocket),&taille,sizeof(int),0)  == -1){
		fprintf(stderr, "Client : erreur miseAjourRessource recv taille :\n");
		perror("");
	}
	printf("taille %d\n", taille);
	struct ressources * ressourcesDispo = (struct ressources*) malloc(sizeof(struct ressources)*taille);
	memset(ressourcesDispo, 0, taille);
	// TODO fonction pour tout envoyer la structure
	for(int i=0;i<taille;i++){
		if(recv(*(paramThread->descripteurSocket),&ressourcesDispo[i],sizeof(struct ressources),0) == -1){
			fprintf(stderr, "Client : erreur miseAjourRessource recv ressourceDispo :\n");
			perror("");
		}
	}
	printf("ressourceDispo[0].label %d\n", ressourcesDispo[0].nbProcesseurs);
	int tailleIndModif;
	int indCourant;
	while(1){
		if(recv(*(paramThread->descripteurSocket),&tailleIndModif,sizeof(int),0)  == -1){
			fprintf(stderr, "Client : erreur miseAjourRessource recv tailleIndModifier:\n");
			perror("");
		}
		for(int i=0;i<tailleIndModif;i++){
			if(recv(*(paramThread->descripteurSocket),&indCourant,sizeof(int),0)  == -1){
				fprintf(stderr, "Client : erreur miseAjourRessource recv indCourant:\n");
				perror("");
			}
			if(recv(*(paramThread->descripteurSocket),&ressourcesDispo[indCourant],sizeof(struct ressources),0) == -1){
				fprintf(stderr, "Client : erreur miseAjourRessource recv ressourceDispo[%d]:\n", indCourant);
				perror("");
			}
		}
		system("clear");
		//pourquoi pas mutex dans son propre thread ?
		affichageRessources(ressourcesDispo, taille);
		// end mutex
		//
	}
	pthread_exit(NULL);
} // thread 1

void *fnt_envoiRequette(void * param){
// protocole :
// des que la saisie est bonne (flagCorrect), alors envoyer l'ID de la ville (idVille),  le nombre de processeurs (nbProc),  le stockage (nbMem) et mode exclusif ou partager (flagPartager)
		struct paramThreadType1 *paramThread = (struct paramThreadType1 *) param;
		//char * chaine = (char *) malloc(sizeof(char)*255);
		int idVille = 0;
		int nbProc = 1;
		int nbMem = 1;
		int flagPartager = 0;
		while(1){
			// TODO
			// affichageRessources(ressourcesDispo, taille);
			// flagParDefautToWait = Yes/No
			// printf("est ce que tu veux saisir des donnees? 1/0 \n"); //oui/non set timer 5S
			//scanf("%d\n", );
			// if (flagWait) alors ce qu'il y a en dessous sinon recommencer un  affichage
			int flagCorrect = 0;
			while (flagCorrect == 0) {
				printf("Client fils %d : Tapez au clavier l'id \33[0;31m(entier >=0)\33[0;0m; de la idVille, le nombre de processeurs desires \33[0;31m(entier >=0)\033[0;0m, la taille de la memoire en Go \33[0;31m(entier >=0)\033[0;0m, et le type de reservation \33[0;31m(0 => exlusif, 1=>partage)\033[0;0m  : \n", getpid());
				printf("\33[0;36mExemple\33[0;0m : 2 155 600 1\n");
				fflush(stdout);
				//int col, lignes;
				get_win_value(&col, &row);
				printf("\033[%d;%dH",col-2,0);
				scanf("%d %d %d %d",&idVille,&nbProc,&nbMem,&flagPartager);
				// TODO : proteger l'input des cas speciaux
				// scanf("%s", chaine);
				// idVille = atoi(chaine);
				// scanf("%s", chaine);
				// nbProc = atoi(chaine);
				// scanf("%s", chaine);
				// nbMem = atoi(chaine);
				// scanf("%s", chaine);
				// flagPartager = atoi(chaine);
				fflush(stdout);
				if(idVille >= 0 || nbProc>=0 || nbMem >=0){
					flagCorrect = 1;
				}else{
					printf("Client fils %d : Erreur de saisi\n", getpid());
					fflush(stdout);
				}
				printf("Client fils %d : Confirmez ? \33[0;31m0\033[0;30m/\33[0;33m1\033[0;0m \n", getpid());
				fflush(stdout);
				printf("\033[%d;%dH",col-2,0);
				scanf("%d", &flagCorrect);
				// scanf("%s", chaine);
				// flagCorrect = atoi(chaine);
				fflush(stdout);
				if(flagCorrect != 0){
					// TODO : CRC ou structure
					printf("ville %d proc %d stock %d flagPartager %d flagCorrect %d\n",idVille, nbProc, nbMem, flagPartager, flagCorrect );
					if(send(*(paramThread->descripteurSocket),&idVille,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send idVille:\n");
						perror("");
					}
					if(send(*(paramThread->descripteurSocket),&nbProc,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send nbProc:\n");
						perror("");
					}
					if(send(*(paramThread->descripteurSocket),&nbMem,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send nbMem:\n");
						perror("");
					}
					if(send(*(paramThread->descripteurSocket),&flagPartager,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send flagPartager:\n");
						perror("");
					}
				}
				else{
					printf("Client fils %d :: Requete non envoyee, corrigez votre saisie\n", getpid());
					printf("ville %d proc %d stock %d flagPartager %d flagCorrect %d\n",idVille, nbProc, nbMem, flagPartager, flagCorrect );
				}
				printf("\033[u");
			}
			// TODO recv la reponse de prise en charge + accepter/refuser + historique des reservations
		}
		pthread_exit(NULL);
} // thread 2

int routineSocket(int * descripteurSocket, struct sockaddr_in *socketAddr, char* ip, int port){
	(*descripteurSocket) = socket(PF_INET, SOCK_STREAM, 0);
	if (*descripteurSocket == -1) {
		fprintf(stderr, "Client : erreur routine socket() :\n");
		perror("");
		return -1;
	}
	(*socketAddr).sin_family = AF_INET;
	inet_pton(AF_INET, ip, &((*socketAddr).sin_addr));
	(*socketAddr).sin_port =  htons(port);
	socklen_t lgaddr = sizeof(struct sockaddr_in);
	if(connect(*descripteurSocket, (struct sockaddr *) socketAddr, lgaddr) == -1){
		fprintf(stderr, "Client : erreur connect :\n");
		perror("");
		if(close(*descripteurSocket) == -1){
			perror("Client : erreur close :");
			return -1;
		}
		return -1;
	}
	printf("Client : \33[0;32m%s:\033[0;0m%d\033[0m\n", inet_ntoa((*socketAddr).sin_addr), ntohs((*socketAddr).sin_port));
	return 0;
}

int main(int argc, char ** argv){
	if (argc != 4) {
		printf("utilisation: %s <IP_SERVEUR> <PORT_SERVEUR> <CLE>\n", argv[0]);
		exit(1);
	}
	//key_t cle = ftok(argv[3], 418);
	int descripteurSocket;
	struct sockaddr_in addrServ;
	char * ip = (char *) malloc(sizeof(char)*16);
	// "16" <==> nombre de char d'une ipv4 + '/0'
	strcpy(ip,argv[1]);
	int port = atoi(argv[2]);
	if(routineSocket(&descripteurSocket, &addrServ, ip, port) == -1){
		fprintf(stderr, "Client : erreur routine socket avec serveur parent\n");
		exit(-1);
	}
	int greetings = 408;
	if(send(descripteurSocket,&greetings,sizeof(int),0) ==-1){
		fprintf(stderr, "Client : erreur send greetings:\n");
		perror("");
		if(close(descripteurSocket) == -1){
			perror("Client : erreur close socket serveur parent:");
			return -1;
		}
		exit(1);
	}
	if(recv(descripteurSocket, &port, sizeof(int), 0) == -1){
		fprintf(stderr, "Client : erreur recv port attribue serveur fils:\n");
		perror("");
	}
	if(port > 1024){
		// 1024 premier port non reserveur au systeme allouable par le serveur fils
		// pseudo-test !
		// fermer la connection avec le serveur "parent"
		if(routineSocket(&descripteurSocket, &addrServ, ip, port) == -1){
			fprintf(stderr, "Client : erreur routine socket serveur fils, port : %d\n", port);
			close(descripteurSocket);
			exit(1);
		}
		// recoit le port du thread etatRessources
		if(recv(descripteurSocket, &port, sizeof(int), 0) == -1){
			fprintf(stderr, "Client : erreur recv port serveur fils etatRessource :\n");
			perror("");
			close(descripteurSocket);
			exit(1);
		}
		// puis nouvelle socket pour le serveur fils pour le thread etatRessource
		int descripteurSocketMiseAjour;
		struct sockaddr_in addrServMiseAjour;
		if(routineSocket(&descripteurSocketMiseAjour, &addrServMiseAjour, ip, port) == -1){
			fprintf(stderr, "Client : erreur routine socket serveur fils etatResosurce, port : %d\n", port);
			close(descripteurSocket);
			exit(1);
		}
		// recoit le port du thread requeteRessource
		if(recv(descripteurSocket, &port, sizeof(int), 0) == -1){
			fprintf(stderr, "Client : erreur recv port serveur fils requeteclient :\n");
			perror("");
			close(descripteurSocket);
			exit(1);
		}
		// puis nouvelle socket avec le serveur fils pour le thread requeteRessource
		int descripteurSocketRequeteRessource;
		struct sockaddr_in addrServRequeteRessource;
		if(routineSocket(&descripteurSocketRequeteRessource, &addrServRequeteRessource, ip, port) == -1){
			fprintf(stderr, "Client : erreur routine socket serveur fils requeteclient, port : %d\n", port);
			close(descripteurSocket);
			exit(1);
		}
		// threads => etatRessource + requeteRessource
		int nbThreads = 2;
		pthread_t * threads =(pthread_t*)malloc(nbThreads*sizeof(pthread_t));
		struct paramThreadType1 *paramEtatRessources = (struct paramThreadType1*) malloc(sizeof(struct paramThreadType1));
		paramEtatRessources->descripteurSocket=&descripteurSocketMiseAjour;
		if (pthread_create(&threads[0],NULL,fnt_miseAjourDisporessources,paramEtatRessources) != 0) {
			fprintf(stderr, "Client : erreur creation thread miseAjourRessource");
			perror("");
			exit(1);
		}
		struct paramThreadType1 * paramRequeteRessources = (struct paramThreadType1*) malloc(sizeof(struct paramThreadType1));
		paramRequeteRessources->descripteurSocket=&descripteurSocketRequeteRessource;
		if (pthread_create(&threads[1],NULL,fnt_envoiRequette,paramRequeteRessources) != 0) {
			fprintf(stderr, "Client : erreur creation thread requeteRessource");
			perror("");
			exit(1);
		}
	}else{
		fprintf(stderr, "Client: erreur attribution port du serveur fils : %d\n", port);
		exit(1);
	}
	while(1){}
	return 0;
}
