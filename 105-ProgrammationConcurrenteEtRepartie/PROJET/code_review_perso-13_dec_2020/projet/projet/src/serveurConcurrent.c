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

#define MAGIC_WORD 408
#define NB_CLIENT 50
#define BUF_SIZE 255
// SIGINT deja dans signal.h ?
#define SIGINT 2
#define SIGQUIT 3

// Variable Globale
int											taille;
int											pidParent;
int											pids[NB_CLIENT];
int											sockfd;
int											msgid;
int											shmId;
int											semId;
key_t										memKey;

struct memoirePartager*	ptrShm;
struct mesg_buffer messageQueue;
pthread_cond_t condRessource = PTHREAD_COND_INITIALIZER;
pthread_mutex_t verrouRessource = PTHREAD_MUTEX_INITIALIZER;

// structure for message queue
struct mesg_buffer {
    long mesg_type;
		int nbProcesseur;
		int nbCapaciteStockage;
};

struct contenu_msg {
	int nbProcesseur;
	int nbCapaciteStockage;
};

struct site {
	char label[BUF_SIZE];
	int nbProcesseur;//les ressources libres
	int nbCapaciteStockage;//les ressources libres
	int nbProcesseurExclusif;
	int nbCapaciteStockageExclusif;
	int nbProcesseurPartage; // initialise a nbProc
	int nbCapaciteStockagePartage; // init ...
} __attribute__((packed));

struct memoirePartager {
	int taille;
	struct site sitesDispo[1024];
};

struct reservation {
	int label;
	int nbProcesseur;
	int nbCapaciteStockage;
	int estPartage; //0 exclusif 1 partage
	int estLiberation; //0 demande 1 liberation
} __attribute__((packed));

union semun {
	int              val;    /* Valeur pour SETVAL */
	struct semid_ds *buf;    /* Tampon pour IPC_STAT, IPC_SET */
	unsigned short  *array;  /* Tableau pour GETALL, SETALL */
	struct seminfo  *__buf;  /* Tampon pour IPC_INFO (specifique à Linux) */
};

void doprocessing (int sock);

void initSitesManuel(struct site * sitesDispo, int* taille);

void initSites(struct site * sitesDispo, int* taille);

void affichageSites(struct site * sitesDispo, int taille);

void *fnt_requeteCliente(void * param);

void *fnt_etatSites(void * param);

int P(int semId, int idVille);

int PNonBloquant(int semId, int idVille);

int V(int semId, int idVille);

void gestionSigInt(int sig);

void gestionSigQuit(int sig);

int ressourceRestant(int label, int type);

int main( int argc, char *argv[] ) {
	pidParent = getpid();
	// int sockfd;
	int newsockfd, portno, clilen;
	struct sockaddr_in serv_addr, cli_addr;
	// int pids[NB_CLIENT];
	int cptPid = 0;
	union semun cmdSemInitSites;
	if (argc != 4) {
		printf("utilisation: %s <PORT> <CLE> <0/1>\n", argv[0]);
		exit(1);
	}
	memKey = ftok(argv[2], 419);
	if (memKey == -1) {
		perror("Serveur : erreur ftok");
		exit(1);
	}
	msgid = msgget(memKey, 0666 | IPC_CREAT);
	messageQueue.mesg_type = 1;
	//messageQueue.nbDeLecture = 0; // peut etre surperflue, mais utile pour faire une trace quand un thread d'un serveur fils remet le mesg dans la queue
	shmId = shmget(memKey,(size_t) sizeof(struct memoirePartager), IPC_CREAT | 0666);
	if (shmId < 0) {
		perror("Serveur : SHARED MEMORY, init\n");
		exit(1);
	}
	ptrShm = shmat(shmId, NULL, 0);
	if((void*) ptrShm == (void*)-1){
		perror("Serveur : erreur shmat :");
		exit(1);
	}
	if(atoi(argv[3]) != 0){
		initSitesManuel(ptrShm->sitesDispo, &taille);}
	else{
		initSites(ptrShm->sitesDispo, &taille);
	}
	// I/O sur la SharedMemory
	// { sem0 .. semN } N => nb sites
	semId = semget(memKey, taille, IPC_CREAT | 0666);
	cmdSemInitSites.array = (unsigned short*) malloc(sizeof(unsigned short)*taille);
	if(semId == -1) {
		perror("Serveur : erreur semget");
		exit(1);
	}
	for (size_t i = 0; i < (size_t)taille; i++) {
		cmdSemInitSites.array[i] = 1;
	}
	if(semctl(semId, 0, SETALL, cmdSemInitSites) == -1){
		perror("Serveur : erreur semctl init");
		exit(1);
	}
	printf("Serveur : Ensemble de Semaphore initialise\n");
	// en gros, sauf que malloc ça tape dans l'espace memoire du process,
	//  =  malloc(sizeof(struct sites) * taille);
	//initSites(ptrShm->sitesDispo, taille);
	affichageSites(ptrShm->sitesDispo, taille);
	/* First call to socket() function */
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0) {
			perror("Serveur : erreur socket");
			exit(1);
	}
	/* Initialize socket structure */
	bzero((char *) &serv_addr, sizeof(serv_addr));
	portno = atoi(argv[1]);
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = INADDR_ANY;
	serv_addr.sin_port = htons(portno);
	/* Now bind the host address using bind() call.*/
	if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
			perror("Serveur : erreur bind");
			exit(1);
	}
	/* Now start listening for the clients, here
			* process will go in sleep mode and will wait
			* for the incoming connection
	*/
	listen(sockfd,NB_CLIENT);
	clilen = (socklen_t)sizeof(cli_addr);

// TODO pour la gestion de SIGINT :
// KILL LES SERVEURS FILS :=> garder les PID'S du FORK + waitpid a rajouter
// close les objets IPCS et al
// enfin EXIT(0) + message de fin

	signal(SIGINT, gestionSigInt);
	signal(SIGQUIT, gestionSigQuit);

	while (1) {
			newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, (socklen_t*)&clilen);
			if (newsockfd < 0) {
				perror("Serveur : erreur accept");
				exit(1);
			}
			/* Create child process */
			pids[cptPid] = fork();
			if (pids[cptPid] < 0) {
				perror("Serveur : erreur fork");
				exit(1);
			}
			if (pids[cptPid] == 0) {
				/* This is the client process */
				close(sockfd);
				doprocessing(newsockfd);
				exit(0);
			}
			else {
				// parent
				close(newsockfd);
				cptPid++;
				if (cptPid > NB_CLIENT) {
					cptPid = 0;
					while (cptPid < NB_CLIENT) {
						// on tue les zombies
						waitpid(pids[cptPid++], NULL, 0);
					}
					cptPid = 0;
				}
			}
	} /* end of while */
	free(cmdSemInitSites.array);
	close(sockfd);
	return 0;
}
void doprocessing (int sock) {
	if(send(sock, &(ptrShm->taille), sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur : erreur send taille sitesDispo :\n");
		perror("");
	}
	int nbThreads = 2;
	pthread_t * threads;
	threads = (pthread_t*)malloc(nbThreads*sizeof(pthread_t));
	if (pthread_create(&threads[0],NULL,fnt_etatSites,&sock) != 0) {
		fprintf(stderr, "Serveur fils %d : erreur creation thread etatRessource", getpid());
		perror("");
		exit(1);
	}
	if (pthread_create(&threads[1],NULL,fnt_requeteCliente,&sock) != 0) {
		fprintf(stderr, "Serveur fils %d : erreur creation thread requeteClient", getpid());
		perror("");
		exit(1);
	}
	while (1) {
		pthread_join(threads[1],NULL);
		if(pthread_kill(threads[0], SIGKILL) != 0){
			fprintf(stderr, "Serveur fils %d : pthread_kill a planter\n",getpid() );
			perror("");
		}
		close(sock);
		free(threads);
		printf("Serveur fils %d : client deconnecter, je termine proprement\n", getpid());
		free(threads);
		exit(0);
	}
}
void initSitesManuel(struct site * sitesDispo, int* taille){
	printf("Combien Voulez vous de sites ?\n" );
	scanf("%d",taille);
	ptrShm->taille = *taille;
	memset(sitesDispo, 0, (*taille)*sizeof(struct site));
	printf("Entrez succesivement le nom, le nombre de processeurs et le nombre de Gio de stockage par site\n" );
	for(int i=0; i<(*taille);i++){
	scanf("%s %d %d", sitesDispo[i].label, &(sitesDispo[i].nbProcesseur), &(sitesDispo[i].nbCapaciteStockage));
	sitesDispo[i].nbProcesseurExclusif = 0;
	sitesDispo[i].nbCapaciteStockageExclusif = 0;
	sitesDispo[i].nbProcesseurPartage = 0;
	sitesDispo[i].nbCapaciteStockagePartage = 0;
	}
}
void initSites(struct site * sitesDispo, int* taille){
	// Exemple
	*taille = 10;
	ptrShm->taille = *taille;
	char nomVilles[10][BUF_SIZE] =  {"Montpellier","Toulouse","Lyon","Marseille","Nice","Nantes","Strasbourg","Bordeaux","Lille","Rennes"};
	int nbProcesseurs[10]={240,140,380,190,290,310,100,90,107,25};
	int nbCapaciteStockage[10]={1500,800,900,3000,780,4000,2700,800,1700,400};
	for(int i=0;i<*taille;i++){
		strcpy(sitesDispo[i].label, nomVilles[i]);
	}
	for(int i=0;i<*taille;i++){
		sitesDispo[i].nbProcesseur = nbProcesseurs[i];
	}
	for(int i=0;i<*taille;i++){
		sitesDispo[i].nbCapaciteStockage = nbCapaciteStockage[i];
	}
	for (size_t i = 0; i < (size_t)*taille; i++) {
		sitesDispo[i].nbProcesseurPartage = 0;
		sitesDispo[i].nbCapaciteStockagePartage = 0;
		sitesDispo[i].nbProcesseurExclusif = 0;
		sitesDispo[i].nbCapaciteStockageExclusif = 0;
		sitesDispo[i].nbProcesseurPartage = 0;
		sitesDispo[i].nbCapaciteStockagePartage = 0;
	}

}
void affichageSites(struct site * sitesDispo, int taille){
	//system("clear");
	printf("%-5s %-16s %-6s %-6s %-6s %-6s %-6s %-6s\n", "ID", "Label", "CPU-T","Go-T","CPU-E", "Go-E","CPU-P","Go-P");
	printf("---------------------------------------------------------------\n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		printf("%-5d %-16s %-6d %-6d %-6d %-6d %-6d %-6d\n", (int)i, sitesDispo[i].label, sitesDispo[i].nbProcesseur, sitesDispo[i].nbCapaciteStockage, sitesDispo[i].nbProcesseurExclusif, sitesDispo[i].nbCapaciteStockageExclusif, sitesDispo[i].nbProcesseurPartage, sitesDispo[i].nbCapaciteStockagePartage );
	}
	printf("---------------------------------------------------------------\n");
}
int P(int semId, int idVille){
	struct sembuf *sopsP = (struct sembuf*) malloc(sizeof(struct sembuf));
	sopsP->sem_num = idVille;
	sopsP->sem_op = -1;
	sopsP->sem_flg = SEM_UNDO;
	if(semop(semId, sopsP, 1)){
		fprintf(stderr, "Serveur fils %d : erreur semop P\n", getpid());
		perror("");
		return -1;
	}
	return 0;
}

int PNonBloquant(int semId, int idVille){
	struct sembuf *sopsP = (struct sembuf*) malloc(sizeof(struct sembuf));
	sopsP->sem_num = idVille;
	sopsP->sem_op = -1;
	sopsP->sem_flg = IPC_NOWAIT | SEM_UNDO;
	if(semop(semId, sopsP, 1)){
		fprintf(stderr, "Serveur fils %d : erreur semop P\n", getpid());
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
		fprintf(stderr, "Serveur fils %d : erreur semop P\n", getpid());
		perror("");
		return -1;
	}
	return 0;
}

void *fnt_etatSites(void * param){
	int * descripteurSocket = (int *) param;
	struct site * copieSitesDisponibles = (struct site * )malloc(sizeof(struct site) * 1024);
	memset(copieSitesDisponibles, 0, 1024*sizeof(struct site));
	for (int i = 0; i < taille; ++i){
		if(P(semId, i) == -1){
			fprintf(stderr, "Serveur fils %d thread etatsite : erreur semop Proberen \n", getpid());
			perror("");
		}else{
			strcpy(copieSitesDisponibles[i].label, ptrShm->sitesDispo[i].label);
			copieSitesDisponibles[i].nbProcesseur = ptrShm->sitesDispo[i].nbProcesseur;
			copieSitesDisponibles[i].nbCapaciteStockage = ptrShm->sitesDispo[i].nbCapaciteStockage;
			copieSitesDisponibles[i].nbProcesseurPartage = ptrShm->sitesDispo[i].nbProcesseurPartage;
			copieSitesDisponibles[i].nbCapaciteStockagePartage = ptrShm->sitesDispo[i].nbCapaciteStockagePartage;
			copieSitesDisponibles[i].nbProcesseurExclusif = ptrShm->sitesDispo[i].nbProcesseurExclusif;
			copieSitesDisponibles[i].nbCapaciteStockageExclusif = ptrShm->sitesDispo[i].nbCapaciteStockageExclusif;
					}
		if (V(semId, i) == -1) {
			fprintf(stderr, "Serveur fils %d thread etatsite : erreur semop Verhogen\n", getpid());
			perror("");
		}
	}
	// on pourrait utiliser une structure pour concatener les valeurs
	for (size_t i = 0; i < (size_t)taille; i++) {
		if(send(*descripteurSocket, &copieSitesDisponibles[i].label, sizeof(char)*255, 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] LABEL :\n", getpid(), (int)i);
		perror("");
		}
		if(send(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseur, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] CPU :\n", getpid(), (int)i);
		perror("");
		}
		if(send(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] GO :\n", getpid(), (int)i);
		perror("");
		}
		if(send(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseurExclusif, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] CPU :\n", getpid(), (int)i);
		perror("");
		}
		if(send(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockageExclusif, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] GO :\n", getpid(), (int)i);
		perror("");
		}
		if(send(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseurPartage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] CPU :\n", getpid(), (int)i);
		perror("");
		}
		if(send(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockagePartage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] GO :\n", getpid(), (int)i);
		perror("");
		}
	}
	int indiceModifier[taille];
	memset(indiceModifier, 0, taille*sizeof(int));
	int tailleIndiceModifier = 0;
	int nbDeVariableModifier = 0;
	// tableau des indices modifier, valeur possible {0,1}
	while(1){
		sleep(5);
		for (int i = 0; i < taille; ++i){
			if(P(semId, i) < 0){
				fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Proberen \n", getpid());
				perror("");
			}
			if(copieSitesDisponibles[i].nbProcesseurExclusif != ptrShm->sitesDispo[i].nbProcesseurExclusif){
				// pour l'instant on a 1 label unique
				// printf("%s cpuLEx %d cpuPtrEX %d indice[%d] %d nbVar %d\n",copieSitesDisponibles[i].label, copieSitesDisponibles[i].nbProcesseurExclusif, ptrShm->sitesDispo[i].nbProcesseurExclusif, i, indiceModifier[i], nbDeVariableModifier );
				copieSitesDisponibles[i].nbProcesseurExclusif = ptrShm->sitesDispo[i].nbProcesseurExclusif;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if(copieSitesDisponibles[i].nbCapaciteStockageExclusif != ptrShm->sitesDispo[i].nbCapaciteStockageExclusif){
				// pour l'instant on a 1 label unique
				// printf("%s GOLEx %d GOPtrEX %d indice[%d] %d nbVar %d\n",copieSitesDisponibles[i].label, copieSitesDisponibles[i].nbCapaciteStockageExclusif, ptrShm->sitesDispo[i].nbCapaciteStockageExclusif, i, indiceModifier[i], nbDeVariableModifier );
				copieSitesDisponibles[i].nbCapaciteStockageExclusif = ptrShm->sitesDispo[i].nbCapaciteStockageExclusif;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if(copieSitesDisponibles[i].nbProcesseurPartage != ptrShm->sitesDispo[i].nbProcesseurPartage){
				// pour l'instant on a 1 label unique
				// printf("%s cpuLP %d cpuPtrP %d indice[%d] %d nbVar %d\n",copieSitesDisponibles[i].label, copieSitesDisponibles[i].nbProcesseurPartage, ptrShm->sitesDispo[i].nbProcesseurPartage, i, indiceModifier[i], nbDeVariableModifier );
				copieSitesDisponibles[i].nbProcesseurPartage = ptrShm->sitesDispo[i].nbProcesseurPartage;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if(copieSitesDisponibles[i].nbCapaciteStockagePartage != ptrShm->sitesDispo[i].nbCapaciteStockagePartage){
				// pour l'instant on a 1 label unique
				// printf("%s GOLP %d GOPtrEx %d indice[%d] %d nbVar %d\n",copieSitesDisponibles[i].label, copieSitesDisponibles[i].nbCapaciteStockagePartage, ptrShm->sitesDispo[i].nbCapaciteStockagePartage, i, indiceModifier[i], nbDeVariableModifier );
				copieSitesDisponibles[i].nbCapaciteStockagePartage = ptrShm->sitesDispo[i].nbCapaciteStockagePartage;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if (V(semId, i) == -1) {
				fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Verhogen\n", getpid());
				perror("");
			}
			if (nbDeVariableModifier > 0){
				// on a eu 1 modification au moins alors
				tailleIndiceModifier++;
			}
		}
		if(send(*descripteurSocket, &tailleIndiceModifier, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur send tailleDesSitesModifier :\n", getpid());
			perror("");
		}
		/*	indiceModifier contient le flag
		*		tailleIndiceModifier ==  combien de flag on doit parcourir
		*		copieSitesDisponibles ==  au information a parcourir selon indiceModifier
		*/
		for (size_t i = 0; i < (size_t)taille; i++) {
			// on pourrait concatener les deux valeurs dans une sturcture ...
			if(indiceModifier[i] == 1){
				// on a un flag pour send copieRessource[i]
				if(send(*descripteurSocket, &i, sizeof(int), 0) < 0){
					fprintf(stderr, "Serveur fils %d : erreur send  indice <%d> de la ressource a modifier :\n", getpid(), (int)i);
				}
				if(send(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseurExclusif, sizeof(int), 0) == -1){
					fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] CPUE :\n", getpid(), (int)i);
				perror("");
				}
				if(send(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockageExclusif, sizeof(int), 0) == -1){
					fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] GOE :\n", getpid(), (int)i);
				perror("");
				}
				if(send(*descripteurSocket, &copieSitesDisponibles[i].nbProcesseurPartage, sizeof(int), 0) == -1){
					fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] CPUP :\n", getpid(), (int)i);
				perror("");
				}
				if(send(*descripteurSocket, &copieSitesDisponibles[i].nbCapaciteStockagePartage, sizeof(int), 0) == -1){
					fprintf(stderr, "Serveur fils %d : erreur send copieSitesDisponibles[%d] GOP :\n", getpid(), (int)i);
				perror("");
				}
			}
		}
		memset(indiceModifier, 0, taille*sizeof(int));
		tailleIndiceModifier = 0;
		nbDeVariableModifier = 0;
	}
	free(copieSitesDisponibles);
	// TODO close IPCS et al
	printf("Serveur fils %d : le thread d'ecoute de requete, a été tué, je termine proprement\n", getpid());
	pthread_exit(NULL);
}

void *fnt_requeteCliente(void * param){
	int *descripteurSocket = ( int *) param;
	int flagExit = 0; // set avec une reservation == -1
	int estCondWaitRessource = 1;
	struct reservation bufReservation;
// TODO HISTORIQUE DES REQUETES
	int i = 0;
	while(flagExit == 0){
		printf("Serveur fils %d : ecoute %d\n",getpid(), ++i );
		if(recv(*descripteurSocket, &bufReservation, sizeof(struct reservation), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient :\n", getpid());
			perror("");
		}
		printf("Serveur fils %d :	label	%d\n", getpid(), bufReservation.label);
		printf("Serveur fils %d :	cpu	%d\n", getpid(), bufReservation.nbProcesseur);
		printf("Serveur fils %d :	go	%d\n", getpid(), bufReservation.nbCapaciteStockage);
		printf("Serveur fils %d :	mode	%d\n", getpid(), bufReservation.estPartage);
		printf("Serveur fils %d :	type	%d \n", getpid(), bufReservation.estLiberation);
		printf("--------------\n" );
		if(bufReservation.label != -1){
			P(semId,bufReservation.label);
			printf("%d reservation \n",getpid() );
			// printf("reservation %d / %d\n", bufReservation.nbProcesseur, bufReservation.nbCapaciteStockage );
			// printf("enPtrShm %d / %d\n", ptrShm->sitesDispo[bufReservation.label].nbProcesseur,ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockage );
			// un premier test qui verifie si la demande est realisable si tout les ressources sont disponibles
			if (bufReservation.nbProcesseur < ptrShm->sitesDispo[bufReservation.label].nbProcesseur && bufReservation.nbCapaciteStockage < ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockage) {
				printf("%d estPossible \n",getpid() );
				estCondWaitRessource = 1;
				while(estCondWaitRessource == 1){
					printf("%d ??? \n",getpid() );
					if(bufReservation.estPartage == 1){
						printf("%d partages \n",getpid() );
						//partage
						if(bufReservation.estLiberation == 1){
							// TODO ??
							// pthread_cond_broadcast(&condRessource);// pas tres utile
							printf("Serveur fils %d : requete de liberation de prise partager\n",getpid() );
						}
						else{
							printf("Serveur fils %d : requete de prise partager \n",getpid() );
							if(ptrShm->sitesDispo[bufReservation.label].nbProcesseurPartage < bufReservation.nbProcesseur && ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockagePartage < bufReservation.nbCapaciteStockage){
								ptrShm->sitesDispo[bufReservation.label].nbProcesseurPartage += bufReservation.nbProcesseur - ptrShm->sitesDispo[bufReservation.label].nbProcesseurPartage;
								ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockagePartage += bufReservation.nbCapaciteStockage - ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockagePartage;
							}
							else{
								V(semId,bufReservation.label);
								pthread_mutex_lock(&verrouRessource);
								printf("%d partager j'ai lock \n",getpid() );
								printf("Serveur fils %d : je ne peux pas satisfaire la demande en partager du client, je m'en dors\n", getpid());
								pthread_cond_wait(&condRessource, &verrouRessource);
								pthread_mutex_unlock(&verrouRessource);
								continue; // skip l'iteration complete pour le while imbriquer
							}
							V(semId,bufReservation.label);
						}
					}
					else{
						printf("%d exclusif \n",getpid() );
						//exclusif
						//TODO : test de limite <0 && >capacite
						if(bufReservation.estLiberation == 1){
							//liberation
							printf("Serveur fils %d : requete de liberation de cpuEX %d / %d  goEX %d / %d\n", getpid(), bufReservation.nbProcesseur,	ressourceRestant(bufReservation.label, 0) ,bufReservation.nbCapaciteStockage , ressourceRestant(bufReservation.label, 1));
							ptrShm->sitesDispo[bufReservation.label].nbProcesseurExclusif -= bufReservation.nbProcesseur;
							ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockageExclusif -= bufReservation.nbCapaciteStockage;
							messageQueue.nbProcesseur = bufReservation.nbProcesseur;
							messageQueue.nbCapaciteStockage = bufReservation.nbCapaciteStockage;
							msgsnd(msgid, &messageQueue, sizeof(messageQueue), 0);
							printf("%d j'ai révéiller tout le monde \n", getpid() );
							V(semId,bufReservation.label);
						}
						else{
							//prise
							printf("Serveur fils %d : requete de prise de cpuEX %d / %d  goEX %d / %d\n", getpid(), bufReservation.nbProcesseur,ressourceRestant(bufReservation.label, 0) ,bufReservation.nbCapaciteStockage, ressourceRestant(bufReservation.label, 1));
							if (ptrShm->sitesDispo[bufReservation.label].nbProcesseurExclusif + bufReservation.nbProcesseur  < ptrShm->sitesDispo[bufReservation.label].nbProcesseur && ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockageExclusif + bufReservation.nbCapaciteStockage < ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockage) {
								ptrShm->sitesDispo[bufReservation.label].nbProcesseurExclusif += bufReservation.nbProcesseur;
								ptrShm->sitesDispo[bufReservation.label].nbCapaciteStockageExclusif += bufReservation.nbCapaciteStockage;
							}
							else{
								V(semId,bufReservation.label);
								printf("Serveur fils %d : je ne peux pas satisfaire la demande en exclusif du client, je m'endors\n", getpid());
								msgrcv(msgid, &messageQueue, sizeof(messageQueue), 1, 0); // 1 signifie un msg de type "reveil"
								if (messageQueue.nbCapaciteStockage < bufReservation.nbCapaciteStockage && messageQueue.nbProcesseur < bufReservation.nbProcesseur) {
									msgsnd(msgid, &messageQueue, sizeof(messageQueue), 0);
								}
								printf("%d je me reveille\n", getpid());
								sleep(1);
								/* DEPRECIER
								on prevois que si un processus lis le message et eviter qu'il le relise juste apres la fin de son iteration,
								il dors 1 seconde apres avoir remis le message pour que quelqu'un d'autre puisse le lire. Et donc laisser ça chance au cas ou il ne peut pas reserver au final
								*/
								continue; // skip l'iteration
							}
							V(semId,bufReservation.label);
						}
					}
					estCondWaitRessource = 0;
					printf("%d je passe a la requete suivante\n",getpid() );
				}
			}
			else {
				// dans ce cas le client a fait une demande impossible a realiser
				// on ne fais donc rien !
				printf("Serveur fils %d : Mon client en demande trop, j'ignore la requete\n", getpid() );
			}
			V(semId,bufReservation.label);
		}
		else{
			// supposer deconnexion
			flagExit = 1;
		}
		bufReservation.label = -2;
		bufReservation.nbProcesseur = 0;
		bufReservation.nbCapaciteStockage = 0;
		bufReservation.estPartage = 0;
		bufReservation.estLiberation = 0;
		affichageSites(ptrShm->sitesDispo, ptrShm->taille);
	}
	printf("Serveur fils %d : je termine le thread d'ecoute client proprement\n", getpid());
	pthread_exit(NULL);
}

void gestionSigInt(int sig){
	// acceder au pids des forks pour les closes
	for (size_t i = 0; i < NB_CLIENT; i++) {
		kill(pids[i], SIGQUIT);
	}
	// fermer la/les sockets
	close(sockfd);
	// fermer la file de messages
	msgctl(msgid, IPC_RMID, NULL);
	// fermer la memoirePartager
	// peut etre que le NULL c'est pas bon, man ==> indique que buf est ignorer
	shmctl(shmId, IPC_RMID, NULL);
	shmdt(ptrShm);
	// fermer les semaphores
	// peut etre que le NULL c'est pas bon, man ==> indique que buf est ignorer
	semctl(semId, 0, IPC_RMID);
	// TODO ??
	// free les malloc

	printf("Serveur : bye\n");
	exit(0);
}



void gestionSigQuit(int sig){
	// pidParent == getpid() alors SIGINT pour KILL les enfants d'abords
	if (pidParent == getpid()) {
		// printf("pid parent %d getpid %d\n", pidParent, getpid());
		raise(SIGINT);
	}else{
		//enfant du parent
		shmctl(shmId, IPC_RMID, NULL);
		shmdt(ptrShm);
		semctl(semId, 0, IPC_RMID);
	}
}


int ressourceRestant(int label, int type){
	// Attention a n'appeller que si on P() et V() la semaphore sur la shm</ptrShm->
	switch (type) {
		case 0:
			return ptrShm->sitesDispo[label].nbProcesseur - ptrShm->sitesDispo[label].nbProcesseurExclusif - ptrShm->sitesDispo[label].nbProcesseurPartage;
			break;
		case 1:
			return ptrShm->sitesDispo[label].nbCapaciteStockage  - ptrShm->sitesDispo[label].nbCapaciteStockageExclusif - ptrShm->sitesDispo[label].nbCapaciteStockagePartage;
			break;

		default:
			return -1;
	}
}
