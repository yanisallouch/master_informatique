#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
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
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>

#define SIGINT 2
#define SIGQUIT 3
#define MAGIC_WORD 408
#define SIZE_IP 16

// Variable Globale
int col,row;
int sockfd;
pthread_t								thread1;
pthread_t								thread2;
int											shmId = -1;
int											semId = -1;
key_t										memKey = -1;
struct memoirePartager*	ptrShm;
pthread_cond_t condi = PTHREAD_COND_INITIALIZER;
pthread_mutex_t verrou = PTHREAD_MUTEX_INITIALIZER;

union semun {
	int              val;    /* Valeur pour SETVAL */
	struct semid_ds *buf;    /* Tampon pour IPC_STAT, IPC_SET */
	unsigned short  *array;  /* Tableau pour GETALL, SETALL */
	struct seminfo  *__buf;  /* Tampon pour IPC_INFO (specifique à Linux) */
};

struct site {
	char label[255];
	int nbProcesseur;//les ressources libres
	int nbCapaciteStockage;//les ressources libres
	int nbProcesseurExclusif;
	int nbCapaciteStockageExclusif;
	int nbProcesseurPartage; // initialise a nbProc
	int nbCapaciteStockagePartage; // init ...
};


struct reservation {
	int label;
	int nbProcesseur;
	int nbCapaciteStockage;
	int estPartage; //0 exclusif 1 partage
	int estLiberation; //0 demande 1 liberation
} __attribute__((packed));

struct memoirePartager {
	int taille;
	struct site sitesDispo[1024];
};

void get_win_value(int *col, int *row);

int P(int semId, int idVille);

int V(int semId, int idVille);

void affichageRessources(struct site * sitesDispo, int taille);

void *fnt_miseAjourDispoSites(void * param);

void *fnt_envoiRequette(void * param);

void gestionSigInt(int sig);


int main(int argc, char *argv[]) {
	if (argc != 4) {
		printf("utilisation: %s <IP_SERVEUR> <PORT_SERVEUR> <CLE>\n", argv[0]);
		exit(1);
	}
	signal(SIGINT, gestionSigInt);
	// int nbThreads = 2;
	int taille;
	int port = atoi(argv[2]);
	// int greetings = MAGIC_WORD;
	union semun cmdSemInitSites;
	int portno;
	// int n;
	struct sockaddr_in serv_addr;
	//struct hostent *server;
	portno = port;
	// "16" <==> nombre de char d'une ipv4 + '/0'
	char * ip = (char *) malloc(sizeof(char)*SIZE_IP);
	strcpy(ip,argv[1]);
	memKey = ftok(argv[3], 429);
	if (memKey == -1) {
		perror("Client : erreur ftok");
		exit(1);
	}
	/* Create a socket point */
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0) {
			perror("Client : erreur socket");
			exit(1);
	}
	// server = gethostbyname(argv[1]);
	// if (server == NULL) {
	// 		fprintf(stderr,"ERROR, no such host\n");
	// 		exit(0);
	// }
	bzero((char *) &serv_addr, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	// bcopy((char *)server->h_addr, (char *)&, server->h_length);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr);
	serv_addr.sin_port = htons(portno);
	/* Now connect to the server */
	if (connect(sockfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
			perror("Client : erreur connecting");
			exit(1);
	}
	// if(send(descripteurSocketPourServeurParent,&greetings,sizeof(int),0) ==-1){
	// 	fprintf(stderr, "Client : erreur send greetings:\n");
	// 	perror("");
	// 	close(descripteurSocketPourServeurParent);
	// 	exit(1);
	// }
	shmId = shmget(memKey, sizeof(struct memoirePartager), IPC_CREAT | 0666);
	if (shmId < 0) {
		perror("Client : erreur memoire, init");
		exit(1);
	}
	printf("Client : memoire partage creer\n");
	ptrShm = shmat(shmId, NULL, 0);
	if((void*) ptrShm == (void*)-1){
		perror("Client : erreur shmat :");
		exit(1);
	}
	if(recv(sockfd, &taille, sizeof(int), 0) == -1){
		fprintf(stderr, "Client : erreur recv taille :\n");
		perror("");
	}
	ptrShm->taille = taille;
	// I/O sur la SharedMemory
	// { sem0 .. semN } N => nb sites
	semId = semget(memKey, taille, IPC_CREAT | 0666);
	cmdSemInitSites.array = (unsigned short*) malloc(sizeof(unsigned short)*taille);
	if(semId == -1) {
		perror("Client : erreur semget");
		exit(1);
	}
	printf("Client : Semaphore accede ou cree avec succes \n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		cmdSemInitSites.array[i] = 1;
	}
	if(semctl(semId, 0, SETALL, cmdSemInitSites) == -1){
		perror("Client : erreur semctl init");
		exit(1);
	}
	printf("Client : Ensemble de Semaphore initialise\n");
	// threads => etatRessource + requeteRessource
	// threads = (pthread_t*) malloc(sizeof(pthread_t)*nbThreads);
	if (pthread_create(&thread1,NULL,fnt_miseAjourDispoSites,&sockfd) != 0) {
		fprintf(stderr, "Client : erreur creation thread miseAjourRessource");
		perror("");
		exit(1);
	}
	if (pthread_create(&thread2,NULL,fnt_envoiRequette,&sockfd) != 0) {
		fprintf(stderr, "Client : erreur creation thread requeteRessource");
		perror("");
		exit(1);
	}
	int flagExit = 0;
	while(flagExit == 0){
		pthread_join(thread2,NULL);
		// join va raise une EINVAL mais c'est dans le comportement attendu
		if(pthread_kill(thread1, SIGKILL) != 0){
			fprintf(stderr, "Serveur fils %d : pthread_kill a planter\n",getpid() );
			perror("");
		}
		close(sockfd);
		flagExit = 1;
		printf("Client %d : client deconnecter, je termine proprement\n", getpid());
	}
	close(sockfd);
	// free(threads);
	free(ip);
	free(cmdSemInitSites.array);
	shmdt(ptrShm);
	// TODO clear IPC-objects
	printf("Client : je termine proprement\n");
	return 0;
}
void affichageRessources(struct site * sitesDispo, int taille){
	// system("clear");
	printf("%-5s %-16s %-6s %-6s %-6s %-6s %-6s %-6s\n", "ID", "Label", "CPU-T","CPU-E","CPU-P", "Go-T","Go-E","Go-P");
	printf("---------------------------------------------------------------\n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		P(semId, i);
		printf("%-5d %-16s %-6d %-6d %-6d %-6d %-6d %-6d\n", (int)i, sitesDispo[i].label, sitesDispo[i].nbProcesseur, sitesDispo[i].nbCapaciteStockage, sitesDispo[i].nbProcesseurExclusif, sitesDispo[i].nbCapaciteStockageExclusif, sitesDispo[i].nbProcesseurPartage, sitesDispo[i].nbCapaciteStockagePartage );
		V(semId, i);
	}
	printf("---------------------------------------------------------------\n");
}
// void get_win_value(int *col, int *row){
// 	struct winsize        window;
// 	ioctl(0, TIOCGWINSZ, &window);
// /* aussi
// 	ioctl(0, TIOCGSIZE, &win);
// 	C'est la meme chose
// */
// 	*col = window.ws_col;
// 	*row = window.ws_row;
// }
void *fnt_miseAjourDispoSites(void * param){
	int * descripteurSocket = (int *) param;
	int taille = ptrShm->taille;
	struct site * copieSitesDisponibles = (struct site * ) malloc(sizeof(struct site) * 1024);
	memset(copieSitesDisponibles, 0, 1024*sizeof(struct site));
	// on pourrait utiliser une structure pour concatener les valeurs
	for (size_t i = 0; i < (size_t)taille; i++) {
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].label, 255*sizeof(char), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] LABEL :\n", getpid(), (int)i);
		perror("");
		}
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseur, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] CPU :\n", getpid(), (int)i);
		perror("");
		}
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] GO :\n", getpid(), (int)i);
		perror("");
		}
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseurExclusif, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] CPU :\n", getpid(), (int)i);
		perror("");
		}
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockageExclusif, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] GO :\n", getpid(), (int)i);
		perror("");
		}
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseurPartage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] CPU :\n", getpid(), (int)i);
		perror("");
		}
		if(recv(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockagePartage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] GO :\n", getpid(), (int)i);
		perror("");
		}
	}

	for (int i = 0; i < taille; ++i){
		if(P(semId, i) < 0){
			fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Proberen \n", getpid());
			perror("");
		}else{
			strcpy(ptrShm->sitesDispo[i].label,copieSitesDisponibles[i].label);
			ptrShm->sitesDispo[i].nbProcesseur = copieSitesDisponibles[i].nbProcesseur;
			ptrShm->sitesDispo[i].nbCapaciteStockage = copieSitesDisponibles[i].nbCapaciteStockage;
			ptrShm->sitesDispo[i].nbProcesseurExclusif = copieSitesDisponibles[i].nbProcesseurExclusif;
			ptrShm->sitesDispo[i].nbCapaciteStockageExclusif = copieSitesDisponibles[i].nbCapaciteStockageExclusif;
			ptrShm->sitesDispo[i].nbProcesseurPartage = copieSitesDisponibles[i].nbProcesseurPartage;
			ptrShm->sitesDispo[i].nbCapaciteStockagePartage = copieSitesDisponibles[i].nbCapaciteStockagePartage;
		}
		if (V(semId, i) == -1) {
			fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Verhogen\n", getpid());
			perror("");
		}
	}
	pthread_cond_broadcast(&condi);
	int indiceModifier[taille];
	memset(indiceModifier, 0, taille*sizeof(int));
	// tableau des indices modifier, valeur possible {0,1}
	int tailleIndModif;
	int indCourant;
	while(1){
		// TODO utilsier la structure requete
		if(recv(*(descripteurSocket),&tailleIndModif,sizeof(int),0)  == -1){
			fprintf(stderr, "Client : erreur miseAjourRessource recv tailleIndModifier:\n");
			perror("");
		}
		//printf("j'ai recu %d tailleIndicieAMdoifier\n", tailleIndModif );
		for(int i=0;i<tailleIndModif;i++){
			// printf(" indice a modifier avant %d \n", indCourant );
			if(recv(*(descripteurSocket),&indCourant,sizeof(int),0)  == -1){
				fprintf(stderr, "Client : erreur miseAjourRessource recv indCourant:\n");
				perror("");
			}
			// printf(" indice a modifier apres %d \n", indCourant );
			if(recv(*descripteurSocket, &copieSitesDisponibles[indCourant].nbProcesseurExclusif, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] CPU :\n", getpid(), (int)i);
				perror("");
			}
			if(recv(*descripteurSocket, &copieSitesDisponibles[indCourant].nbCapaciteStockageExclusif, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] GO :\n", getpid(), (int)i);
				perror("");
			}
			if(recv(*descripteurSocket, &copieSitesDisponibles[indCourant].nbProcesseurPartage, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] CPU :\n", getpid(), (int)i);
				perror("");
			}
			if(recv(*descripteurSocket, &copieSitesDisponibles[indCourant].nbCapaciteStockagePartage, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[%d] GO :\n", getpid(), (int)i);
				perror("");
			}
			if (P(semId, indCourant) == -1){
				fprintf(stderr, "Client fils %d thread etatSites : erreur semop Proberen \n", getpid());
				perror("");
			}
			ptrShm->sitesDispo[indCourant].nbProcesseur = copieSitesDisponibles[indCourant].nbProcesseur;
			ptrShm->sitesDispo[indCourant].nbCapaciteStockage = copieSitesDisponibles[indCourant].nbCapaciteStockage;
			ptrShm->sitesDispo[indCourant].nbProcesseurExclusif = copieSitesDisponibles[indCourant].nbProcesseurExclusif;
			ptrShm->sitesDispo[indCourant].nbCapaciteStockageExclusif = copieSitesDisponibles[indCourant].nbCapaciteStockageExclusif;
			ptrShm->sitesDispo[indCourant].nbProcesseurPartage = copieSitesDisponibles[indCourant].nbProcesseurPartage;
			ptrShm->sitesDispo[indCourant].nbCapaciteStockagePartage = copieSitesDisponibles[indCourant].nbCapaciteStockagePartage;
			if (V(semId, indCourant) == -1) {
				fprintf(stderr, "Client fils %d thread etatSites : erreur semop Verhogen\n", getpid());
				perror("");
			}
		}
	}
	free(copieSitesDisponibles);
	pthread_exit(NULL);
} // thread 1


void *fnt_envoiRequette(void * param){
	int *descripteurSocket = (int *) param;
	descripteurSocket+=0;
	int taille = ptrShm->taille;
	int ville = 0;
	int update = 1;
	int cpu=0, stockage=0, partager=0, liberation=0;
	char* saisie = (char*) malloc(sizeof(char)*22);
	char *token;
	const char s[2] = "/";
	struct reservation bufReservation;
	pthread_mutex_lock(&verrou);
	pthread_cond_wait(&condi,&verrou);
	pthread_mutex_unlock(&verrou);
	while(ville != -1){
		printf("Update l'affichage \33[0;31m0\33[0;0m ou \33[0;31m1\33[0;0m ?\n" );
		scanf("%d", &update );
		if(update){
			affichageRessources(ptrShm->sitesDispo, taille);
		}else{
			printf("Client : Saisi clavier  \33[0;31midVille >=0\33[0;0m / \33[0;31mCPU >0\33[0;0m /  \33[0;31mStockage >0\33[0;0m / \33[0;31mpartager 0\33[0;0m ou \33[0;31m1\33[0;0m / \33[0;31mliberation 0\33[0;0m; ou \33[0;31m1\33[0;0m; ?\n");
			//scanf("%d %d %d %d %d", &ville, &cpu, &stockage, &partager, &liberation);

			scanf("%s", saisie);
			token = strtok(saisie,s);
			ville = atoi(token);
			token = strtok(NULL,s);
			cpu=atoi(token);
			token = strtok(NULL,s);
			stockage=atoi(token);
			token = strtok(NULL,s);
			partager=atoi(token);
			token = strtok(NULL,s);
			liberation=atoi(token);

			// printf("liberation %d\n", liberation );
			bufReservation.label=ville;
			bufReservation.nbProcesseur=cpu;
			bufReservation.nbCapaciteStockage=stockage;
			bufReservation.estPartage=partager;
			bufReservation.estLiberation=liberation;
			if(send(*descripteurSocket,&bufReservation,sizeof(struct reservation),0) < 0) {
				fprintf(stderr, "Client : erreur envoie requete %d %d %d %d %d \n",  ville, cpu, stockage, partager, liberation);
				perror("");
			}
			bufReservation.nbProcesseur=cpu=0;
			bufReservation.nbCapaciteStockage=stockage=0;
			bufReservation.estPartage=partager=0;
			bufReservation.estLiberation=liberation=0;
		}
	}
	printf("Client : je termine proporement le thread de requete\n");
	pthread_exit(NULL);
} // thread 2


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

void gestionSigInt(int sig){
	pthread_kill(thread1, SIGKILL);
	pthread_kill(thread2, SIGKILL);
	// fermer la/les sockets
	close(sockfd);
	// fermer la memoirePartager
	// peut etre que le NULL c'est pas bon, man ==> indique que buf est ignorer
	shmctl(shmId, IPC_RMID, NULL);
	shmdt(ptrShm);
	// fermer les semaphores
	// peut etre que le NULL c'est pas bon, man ==> indique que buf est ignorer
	semctl(semId, 0, IPC_RMID);
	// TODO ??
	// free les malloc
	printf("Client : bye\n");
	exit(0);
}
