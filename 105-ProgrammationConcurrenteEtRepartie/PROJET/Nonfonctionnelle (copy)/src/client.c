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

#define MAGIC_WORD 408
#define SIZE_IP 16

// Variable Globale
int											shmId = -1;
int											semId = -1;
key_t										memKey = -1;
struct memoirePartager*	ptrShm;

union semun {
	int              val;    /* Valeur pour SETVAL */
	struct semid_ds *buf;    /* Tampon pour IPC_STAT, IPC_SET */
	unsigned short  *array;  /* Tableau pour GETALL, SETALL */
	struct seminfo  *__buf;  /* Tampon pour IPC_INFO (specifique à Linux) */
};

struct sites {
	char label[255];
	int nbProcesseurs;//les ressources libres
	int nbCapaciteStockage;//les ressources libres
	int nbProcesseursPartage;
	int nbCapaciteStockagePartage;
};

struct memoirePartager {
	int taille;
	struct sites sitesDispo[1024];
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

int P(int semId, int idVille){
	struct sembuf *sopsP = (struct sembuf*) malloc(sizeof(struct sembuf));
	sopsP->sem_num = idVille;
	sopsP->sem_op = -1;
	sopsP->sem_flg = SEM_UNDO;
	if(semop(semId, sopsP, 1)){
		fprintf(stderr, "Client fils %d : erreur semop P\n", getpid());
		perror("");
		return -1;
	}
	return 0;
}

int V(int semId, int idVille){
	struct sembuf *sopsV = (struct sembuf*) malloc(sizeof(struct sembuf));
	sopsV->sem_num = idVille;
	sopsV->sem_op = +1;
	sopsV->sem_flg = SEM_UNDO;
	if(semop(semId, sopsV, 1)){
		fprintf(stderr, "Client fils %d : erreur semop P\n", getpid());
		perror("");
		return -1;
	}
	return 0;
}

void affichageRessources(struct sites * sitesDispo, int taille){
	system("clear");
	printf("%-5s %-15s %-11s %-11s\n", "ID", "Label", "Nb-CPU", "Stockage (Go)" );
	printf("-------------------------------------------\n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		printf("%-5d %-15s %-11d %-11d\n", (int)i, sitesDispo[i].label, sitesDispo[i].nbProcesseurs, sitesDispo[i].nbCapaciteStockage );
	}
	printf("-------------------------------------------\n");
}

void *fnt_miseAjourDispoSites(void * param){
	int *descripteurSocket = ( int *) param;
	int taille;
	// protocole :
	// recevoir la taille (taille) totale des sites disponibles
	// recevoir les donnees (sitesDispo) des sites disponibles en integralite
	// tant qu'il est connecter, recevoir le nombre de modifications (tailleIndModif) a faire, puis pour chacunne, son indice (indCourant) et sa nouvelle valeur (sitesDispo[indCourant]);
	if(recv(*descripteurSocket,&taille,sizeof(int),0)  == -1){
		fprintf(stderr, "Client : erreur miseAjourRessource recv taille :\n");
		perror("");
	}
	struct sites * sitesDispo = (struct sites*) malloc(sizeof(struct sites)*taille);
	memset(sitesDispo, 0, taille);
	for (int i = 0; i < taille; ++i){
		if (P(semId, i) == -1){
			fprintf(stderr, "Client fils %d thread etatSites : erreur semop Proberen \n", getpid());
			perror("");
		}else{
			ptrShm->sitesDispo[i] = sitesDispo[i];
		}
		if (V(semId, i) == -1) {
			fprintf(stderr, "Client fils %d thread etatSites : erreur semop Verhogen\n", getpid());
			perror("");
		}
	}
	affichageRessources(sitesDispo, taille);
	// TODO fonction pour tout envoyer la structure
	for(int i=0;i<taille;i++){
		if(recv(*descripteurSocket,&sitesDispo[i],sizeof(struct sites),0) == -1){
			fprintf(stderr, "Client : erreur miseAjourRessource recv ressourceDispo :\n");
			perror("");
		}
	}
	int tailleIndModif;
	int indCourant;
	while(1){
		if(recv(*(descripteurSocket),&tailleIndModif,sizeof(int),0)  == -1){
			fprintf(stderr, "Client : erreur miseAjourRessource recv tailleIndModifier:\n");
			perror("");
		}
		for(int i=0;i<tailleIndModif;i++){
			if(recv(*(descripteurSocket),&indCourant,sizeof(int),0)  == -1){
				fprintf(stderr, "Client : erreur miseAjourRessource recv indCourant:\n");
				perror("");
			}
			if(recv(*(descripteurSocket),&sitesDispo[indCourant],sizeof(struct sites),0) == -1){
				fprintf(stderr, "Client : erreur miseAjourRessource recv ressourceDispo[%d]:\n", indCourant);
				perror("");
			}
			if (P(semId, indCourant) == -1){
				fprintf(stderr, "Client fils %d thread etatSites : erreur semop Proberen \n", getpid());
				perror("");
			}
			else{
				ptrShm->sitesDispo[indCourant] = sitesDispo[indCourant];
			}
			if (V(semId, indCourant) == -1) {
				fprintf(stderr, "Client fils %d thread etatSites : erreur semop Verhogen\n", getpid());
				perror("");
			}
		}
	}
	pthread_exit(NULL);
} // thread 1

void *fnt_envoiRequette(void * param){
// protocole :
// des que la saisie est bonne (flagCorrect), alors envoyer l'ID de la ville (idVille),  le nombre de processeurs (nbProc),  le stockage (nbMem) et mode exclusif ou partager (flagPartager)
		int * descripteurSocket = (int*) param;
		//char * chaine = (char *) malloc(sizeof(char)*255);
		int idVille = 0;
		int nbProc = 1;
		int nbMem = 1;
		int flagPartager = 0;
		while(1){
			// TODO
			// affichageRessources(sitesDispo, taille);
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
					if(send(*(descripteurSocket),&idVille,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send idVille:\n");
						perror("");
					}
					if(send(*(descripteurSocket),&nbProc,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send nbProc:\n");
						perror("");
					}
					if(send(*(descripteurSocket),&nbMem,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send nbMem:\n");
						perror("");
					}
					if(send(*(descripteurSocket),&flagPartager,sizeof(int),0) == -1){
						fprintf(stderr, "Client : erreur send flagPartager:\n");
						perror("");
					}
				}
				else{
					printf("Client fils %d :: Requete non envoyee, corrigez votre saisie\n", getpid());
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
	int nbThreads = 2;
	pthread_t * threads;
	int taille = -1;
	int greetings = MAGIC_WORD;
	int port = atoi(argv[2]);
	int descripteurSocketPourServeurParent = -1;
	int descripteurSocketPourServeurFils = -1;
	int descripteurSocketPourMiseAjour = -1;
	int descripteurSocketPourRequeteRessource = -1;
	struct sockaddr_in addrServ;
	struct sockaddr_in addrServFils;
	struct sockaddr_in addrServMiseAjour;
	struct sockaddr_in addrServRequeteRessource;
	// "16" <==> nombre de char d'une ipv4 + '/0'
	char * ip = (char *) malloc(sizeof(char)*SIZE_IP);
	strcpy(ip,argv[1]);
	memKey = ftok(argv[3], 418);
	if (memKey == -1) {
		perror("Client : erreur ftok");
		exit(1);
	}
	if(routineSocket(&descripteurSocketPourServeurParent, &addrServ, ip, port) == -1){
		fprintf(stderr, "Client : erreur routine socket avec serveur parent\n");
		exit(-1);
	}
	if(send(descripteurSocketPourServeurParent,&greetings,sizeof(int),0) ==-1){
		fprintf(stderr, "Client : erreur send greetings:\n");
		perror("");
		close(descripteurSocketPourServeurParent);
		exit(1);
	}
	if(recv(descripteurSocketPourServeurParent, &port, sizeof(int), 0) == -1){
		fprintf(stderr, "Client : erreur recv port attribue serveur fils:\n");
		perror("");
	}
	if(port > 1024){
		// pseudo-test !
		// 1024 premier port non reserveur au systeme allouable par le serveur fils
		if(routineSocket(&descripteurSocketPourServeurFils, &addrServFils, ip, port) == -1){
			fprintf(stderr, "Client : erreur routine socket serveur fils, port : %d\n", port);
			close(descripteurSocketPourServeurParent);
			exit(1);
		}
		// recoit le port du thread etatRessources
		if(recv(descripteurSocketPourServeurFils, &port, sizeof(int), 0) == -1){
			fprintf(stderr, "Client : erreur recv port serveur fils etatRessource :\n");
			perror("");
		}
		// puis nouvelle socket pour le serveur fils pour le thread etatRessource
		if(routineSocket(&descripteurSocketPourMiseAjour, &addrServMiseAjour, ip, port) == -1){
			fprintf(stderr, "Client : erreur routine socket serveur fils etatResosurce, port : %d\n", port);
		}
		// recoit le port du thread requeteRessource
		if(recv(descripteurSocketPourServeurFils, &port, sizeof(int), 0) == -1){
			fprintf(stderr, "Client : erreur recv port serveur fils requeteclient :\n");
			perror("");
		}
		// puis nouvelle socket avec le serveur fils pour le thread requeteRessource
		if(routineSocket(&descripteurSocketPourRequeteRessource, &addrServRequeteRessource, ip, port) == -1){
			fprintf(stderr, "Client : erreur routine socket serveur fils requeteclient, port : %d\n", port);
		}
		shmId = shmget(memKey, sizeof(struct memoirePartager), IPC_CREAT | 0666);
		if (shmId < 0) {
			perror("Client : SHARED MEMORY, init\n");
			exit(1);
		}
		printf("Client : SHARED MEMORY creer)\n");
		ptrShm = shmat(shmId, NULL, 0);
		if((void*) ptrShm == (void*)-1){
			perror("Client : erreur shmat :");
			exit(1);
		}
		if(recv(descripteurSocketPourServeurFils, &taille, sizeof(int), 0) == -1){
			fprintf(stderr, "Client : erreur recv port serveur fils requeteclient :\n");
			perror("");
		}
		ptrShm->taille = taille;
		// I/O sur la SharedMemory
		// { sem0 .. semN } N => nb sites
		semId = semget(memKey, taille, IPC_CREAT | 0666);
		union semun cmdSemInitSites;
		cmdSemInitSites.array = (unsigned short*) malloc(sizeof(unsigned short)*taille);
		if(semId == -1) {
			perror("Client : erreur semget");
			exit(1);
		}
		printf("Client : Semaphore accede ou cree avec succes :D\n");
		for (size_t i = 0; i < (size_t)taille; i++) {
			cmdSemInitSites.array[i] = 1;
		}
		if(semctl(semId, 0, SETALL, cmdSemInitSites) == -1){
			perror("Client : erreur semctl init");
			exit(1);
		}
		printf("Client : Ensemble de Semaphore initialise\n");
		// threads => etatRessource + requeteRessource
		threads =(pthread_t*)malloc(nbThreads*sizeof(pthread_t));
		if (pthread_create(&threads[0],NULL,fnt_miseAjourDispoSites,&descripteurSocketPourMiseAjour) != 0) {
			fprintf(stderr, "Client : erreur creation thread miseAjourRessource");
			perror("");
			exit(1);
		}
		if (pthread_create(&threads[1],NULL,fnt_envoiRequette,&descripteurSocketPourRequeteRessource) != 0) {
			fprintf(stderr, "Client : erreur creation thread requeteRessource");
			perror("");
			exit(1);
		}
	}else{
		fprintf(stderr, "Client: erreur attribution port du serveur fils : %d\n", port);
		exit(1);
	}
	for (size_t i = 0; i < (size_t)nbThreads; i++) {
		pthread_join(threads[i], NULL);
	}
	return 0;
}
