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

// Variable Globale
int											shmId;
int											semId;
key_t										memKey;
struct memoirePartager*	ptrShm;

struct sites {
	char label[BUF_SIZE];
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
	int estPartager;
};

struct memoirePartager {
	int taille;
	struct sites sitesDispo[1024];
};
//
// struct requeteReservation {
// 	int label;
// 	int nbProcesseur;
// 	int nbCapaciteStockage;
// 	int typePartage; //0 exclusif 1 partage
// 	int typeReservation; //0 demande 1 liberation
// };
//
// struct paramThreadType1 {
// 	int * descripteurSocket;
// };

union semun {
	int              val;    /* Valeur pour SETVAL */
	struct semid_ds *buf;    /* Tampon pour IPC_STAT, IPC_SET */
	unsigned short  *array;  /* Tableau pour GETALL, SETALL */
	struct seminfo  *__buf;  /* Tampon pour IPC_INFO (specifique à Linux) */
};

void initSites(struct sites * sitesDispo, int taille){
	char nomVilles[1024][BUF_SIZE] =  {"Montpellier","Toulouse","Lyon","Marseille","Nice","Nantes","Strasbourg","Bordeaux","Lille","Rennes"};
	int nbProcesseurs[1024]={240,140,380,190,290,310,100,90,107,25};
	int nbCapaciteStockage[1024]={1500,800,900,3000,780,4000,2700,800,1700,400};
	for(int i=0;i<taille;i++){
		strcpy(sitesDispo[i].label, nomVilles[i]);
	}
	for(int i=0;i<taille;i++){
		sitesDispo[i].nbProcesseur = nbProcesseurs[i];
	}
	for(int i=0;i<taille;i++){
		sitesDispo[i].nbCapaciteStockage = nbCapaciteStockage[i];
	}
	for (size_t i = 0; i < (size_t)taille; i++) {
		sitesDispo[i].nbProcesseurPartage = 0;
		sitesDispo[i].nbCapaciteStockagePartage = 0;
	}
}

void affichageSites(struct sites * sitesDispo, int taille){
	system("clear");
	printf("%-15s %-11s %-11s\n", "Label", "Nb-CPU", "Stockage (Go)" );
	printf("-------------------------------------------\n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		printf("%-15s %-11d %-11d\n", sitesDispo[i].label, sitesDispo[i].nbProcesseur, sitesDispo[i].nbCapaciteStockage );
	}
	printf("-------------------------------------------\n");
}

int routineSocket(int * descripteurSocket, struct sockaddr_in *socketServeur, int port){
	// initialisation sur les parametres <ip> / <port>
	*descripteurSocket = socket(PF_INET, SOCK_STREAM, 0);
	if (*descripteurSocket == -1) {
		fprintf(stderr, "Serveur : erreur routine socket()\n");
		perror("");
		return -1;
	}
	//memset(&socketServeur, 0, sizeof(struct sockaddr_in));
	/* Clear structure */
	(*socketServeur).sin_family = AF_INET;
	(*socketServeur).sin_addr.s_addr = INADDR_ANY;
	if(port != 0 ){
		(*socketServeur).sin_port =  htons(port);
	}
	if(bind(*descripteurSocket, (struct sockaddr *) socketServeur, sizeof(*socketServeur)) == -1){
		perror("Serveur : erreur routine bind() :");
		if(close(*descripteurSocket) == -1){
			perror("Serveur : erreur routine close() :");
			return -1;
		}
		return -1;
	}
	if (listen(*descripteurSocket, 50) == -1) {
		fprintf(stderr, "Serveur : erreur routien listen() :\n");
		if(close(*descripteurSocket) == -1){
			perror("Serveur : erreur routine 2-close()   :");
			return -1;
		}
		return -1;
	}
	printf("Serveur : \33[0;32m%s:\033[0;0m%d\033[0m\n", inet_ntoa((*socketServeur).sin_addr), ntohs((*socketServeur).sin_port));
	return 0;
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
	int taille = ptrShm->taille;
	struct sites * copieSitesDisponibles = (struct sites * ) malloc(sizeof(struct sites) * 1024);
	memset(copieSitesDisponibles, 0, 1024*sizeof(struct sites));
	for (int i = 0; i < taille; ++i){
		if(P(semId, i) == -1){
			fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Proberen \n", getpid());
			perror("");
		}else{
			copieSitesDisponibles[i] = ptrShm->sitesDispo[i];
		}
		if (V(semId, i) == -1) {
			fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Verhogen\n", getpid());
			perror("");
		}
	}
	if(send(*descripteurSocket, &taille, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send taille sitesDispo :\n", getpid());
		perror("");
	}
	if(send(*descripteurSocket, &copieSitesDisponibles, sizeof(struct sites)*taille, 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send sitesDispo[*] :\n", getpid());
		perror("");
	}
	int indiceModifier[taille];
	memset(indiceModifier, 0, taille*sizeof(int));
	int tailleIndiceModifier = 0;
	int nbDeVariableModifier = 0;
	// tableau des indices modifier, valeur possible {0,1}
	while(1){
		sleep(1);
		for (int i = 0; i < taille; ++i){
			if(P(semId, i) == -1){
				fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Proberen \n", getpid());
				perror("");
			}else{
				if(copieSitesDisponibles[i].nbProcesseur != ptrShm->sitesDispo[i].nbProcesseur){
					// pour l'instant on a 1 label unique
					copieSitesDisponibles[i].nbProcesseur = ptrShm->sitesDispo[i].nbProcesseur;
					indiceModifier[i] = 1;
					nbDeVariableModifier++;
				}
				if(copieSitesDisponibles[i].nbCapaciteStockage != ptrShm->sitesDispo[i].nbCapaciteStockage){
					// pour l'instant on a 1 label unique
					copieSitesDisponibles[i].nbCapaciteStockage = ptrShm->sitesDispo[i].nbCapaciteStockage;
					indiceModifier[i] = 1;
					nbDeVariableModifier++;
				}
				if(copieSitesDisponibles[i].nbProcesseurPartage != ptrShm->sitesDispo[i].nbProcesseurPartage){
					// pour l'instant on a 1 label unique
					copieSitesDisponibles[i].nbProcesseurPartage = ptrShm->sitesDispo[i].nbProcesseurPartage;
					indiceModifier[i] = 1;
					nbDeVariableModifier++;
				}
				if(copieSitesDisponibles[i].nbCapaciteStockagePartage != ptrShm->sitesDispo[i].nbCapaciteStockagePartage){
					// pour l'instant on a 1 label unique
					copieSitesDisponibles[i].nbCapaciteStockagePartage = ptrShm->sitesDispo[i].nbCapaciteStockagePartage;
					indiceModifier[i] = 1;
					nbDeVariableModifier++;
				}
			}
			if (V(semId, i) == -1) {
				fprintf(stderr, "Serveur fils %d thread etatSites : erreur semop Verhogen\n", getpid());
				perror("");
			}
			if (nbDeVariableModifier > 0){
				// on a eu 1 modification au moins alors
				tailleIndiceModifier++;
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
				if(indiceModifier[i] == 1){
					// on a un flag pour send copieRessource[i]
					if(send(*descripteurSocket, &i, sizeof(int), 0) == -1){
						fprintf(stderr, "Serveur fils %d : erreur send  indice <%d> de la ressource a modifier :\n", getpid(), (int)i);
					}
					if(send(*descripteurSocket, &copieSitesDisponibles[i], sizeof(struct sites), 0) == -1){
						fprintf(stderr, "Serveur fils %d : erreur send <%d>eme valeur de resourceDispo <%s | %d | %d>:\n", getpid(),(int)i, copieSitesDisponibles[i].label,copieSitesDisponibles[i].nbProcesseur,copieSitesDisponibles[i].nbCapaciteStockage);
						perror("");
					}
				}
			}
			memset(indiceModifier, 0, taille*sizeof(int));
			tailleIndiceModifier = 0;
			nbDeVariableModifier = 0;
		}
	}
	pthread_exit(NULL);
}

void *fnt_requeteCliente(void * param){
	int *descripteurSocket = ( int *) param;
	int taille = ptrShm->taille;
	int idVille = 0;
	int nbProcesseur = 1;
	int nbStockage = 1;
	int flagPartager = 0;
	// int priseEnCharge = 666;

// TODO HISTORIQUE DES REQUETES

	while(1){
		// TODO CRC ou structure
		if(recv(*descripteurSocket, &idVille, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient idVille :\n",getpid());
			perror("");
		}
		if(recv(*descripteurSocket, &nbProcesseur, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient nbProc:\n", getpid());
			perror("");
		}
		if(recv(*descripteurSocket, &nbStockage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient nbStockage:\n", getpid());
			perror("");
		}
		if(recv(*descripteurSocket, &flagPartager, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient flagPartager:\n", getpid());
			perror("");
		}
		// TODO send prise en charge +
		// if(send(descripteurSocket,&priseEnCharge,sizeof(int),0) == -1){
		// 	fprintf(stderr, "Serveur fils %d : erreur send requeteClient priseEnCharge:\n", getpid());
		// 	perror("");
		// }	//le recv n'est pas fait
		// 	// accepter/refuser apres prise de semaphore + historique des reservations
		// if(flagPartager){
		// 	//partage
		// 	// TODO HISTIRIQUE
		// 	// 250 total
		// 	// c1 200e
		// 	// c2 50p
		// 	// c3 25e
		// 	// C1 200e ==> P(200)
		// 	// C2  demande p ==>
		// 	// if nbProc-nbProcesseurPartage>0(  P(nbProc -nbProcesseurPartage) && {nbProcesseurPartage+=nbProc;}
		// 	//exclusif
		// 	P(semIdProcesseurs, idVille,nbProcesseur-nbProcesseurPartage);
		//	P(semIdStockages, idVille, );
		//}else{
		// 	//exclusif
		//
		// if(P(semId, idVille ) == 1){
		// 	fprintf(stderr, "Serveur fils %d : erreur requeteClient PriseSem  semIdProcesseur\n", getpid());
		// }

		if(send(*descripteurSocket, &taille, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur fils %d : erreur send  requeteClient taille-sitesDispo :\n", getpid());
				perror("");
		}
	}
	pthread_exit(NULL);
}

void traitementClientParUnFils(int socket){
	int portARouter = 0;
	int portAEnvoyer = 0;
	int nbThreads = 2;
	pthread_t * threads;
	char * buffer = (char *) malloc(sizeof(char)*BUF_SIZE);
	int descripteurClient ;
	int descripteurServeurFils;
	int descripteurServeurFilsEtatSites;
	int descripteurServeurFilsRequetesClient;
	int descripteurClientEtatSites;
	int descripteurClientRequetesClient;
	struct sockaddr_in addrServeurFils;
	struct sockaddr_in socketClientDuThread;
	struct sockaddr_in addrServeurFilsEtatSites;
	struct sockaddr_in addrServeurFilsRequetesClient;
	struct sockaddr_in addrClientDuThreadEtatSites;
	struct sockaddr_in addrClientDuThreadRequetesClient;
	socklen_t tailleSocketAddrIn = sizeof(struct sockaddr_in);

	if(routineSocket(&descripteurServeurFils, &addrServeurFils, portARouter)){
		fprintf(stderr, "Serveur fils %d : erreur routine socket dedie serveur fils ecouteClient\n", getpid());
		exit(1);
	}
	portAEnvoyer = ntohs(addrServeurFils.sin_port);
	// TODO qu'est ce qu'il se passe si je n'arrive pas a seznd le port ? je retry ? copmbien de fois ? while (1)
	if(send(socket, &portAEnvoyer, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send port serveur fils :\n", getpid());
		perror("");
	}
	// je lui ai envoyer le port, je peux fermer la socket associe au server parent du client
	//close(descripteurClient);
	descripteurClient = accept(descripteurServeurFils, (struct sockaddr*) &socketClientDuThread, &tailleSocketAddrIn);
	if(descripteurClient == -1){
		fprintf(stderr, "Serveur fils %d : erreur accept connection socket client :\n", getpid());
		perror("");
	}
	//le client est conneter, je lui envois le port dedie pour le thread etatSites
	if(routineSocket(&descripteurServeurFilsEtatSites, &addrServeurFilsEtatSites, portARouter) == -1){
		fprintf(stderr, "Serveur fils %d: erreur traitementClientParUnFils socket etatRessource serveur fils\n", getpid());
		exit(1);
		// gros doute sur le comportement de l'arret =>> le fork ou le parent avec lui
	}
	portAEnvoyer = addrServeurFilsEtatSites.sin_port;
	printf("maj %d\n", portAEnvoyer );
	if(send(descripteurClient, &portAEnvoyer, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send port addrServeurFilsEtatSites : \n", getpid());
		perror("");
	}
	descripteurClientEtatSites = accept(descripteurServeurFilsEtatSites, (struct sockaddr*) &addrClientDuThreadEtatSites, &tailleSocketAddrIn);
	if(descripteurClientEtatSites == -1){
		fprintf(stderr, "Serveur fils %d : erreur accept connection socket client etatRessource :\n", getpid());
		perror("");
		close(descripteurServeurFils);
		exit(1);
	}
	//le client est conneter, je lui envois le port dedier pour le thread etatSites utilise pour la socket du thread requeteClient
	if(routineSocket(&descripteurServeurFilsRequetesClient, &addrServeurFilsRequetesClient, portARouter) == -1){
		fprintf(stderr, "Serveur fils %d : erreur traitementClientParUnFils socket requeteClient serveur fils\n", getpid());
		exit(1);
	}
	portAEnvoyer = addrServeurFilsRequetesClient.sin_port;
	printf("maj %d\n", portAEnvoyer );
	if(send(descripteurClient, &portAEnvoyer, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send port addrServeurFilsRequetesClient : \n", getpid());
		perror("");
	}
	descripteurClientRequetesClient = accept(descripteurServeurFilsRequetesClient, (struct sockaddr*) &addrClientDuThreadRequetesClient, &tailleSocketAddrIn);
	if(descripteurClientRequetesClient == -1){
		fprintf(stderr, "Serveur fils %d : erreur accept connection socket client requeteClient :\n", getpid());
		perror("");
		close(descripteurServeurFils);
		exit(1);
	}
	// fais partie du protocle pour que le client puisse init sont SHM
	if(send(descripteurClient, &ptrShm->taille, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send taille : \n", getpid());
		perror("");
	}
	threads = (pthread_t*)malloc(nbThreads*sizeof(pthread_t));
	if (pthread_create(&threads[0],NULL,fnt_etatSites,&descripteurClientEtatSites) != 0) {
		fprintf(stderr, "Serveur fils %d : erreur creation thread etatRessource", getpid());
		perror("");
		exit(1);
	}
	if (pthread_create(&threads[1],NULL,fnt_requeteCliente,&descripteurClientRequetesClient) != 0) {
		fprintf(stderr, "Serveur fils %d : erreur creation thread requeteClient", getpid());
		perror("");
		exit(1);
	}
	while (1) {
		if (recv(descripteurClient, &buffer, sizeof(char)*BUF_SIZE, 0) == -1) {
			if(strcmp(buffer, "exit") == 0){
				if(pthread_kill(threads[0], SIGKILL) != 0){
					fprintf(stderr, "Serveur fils %d : pthread_kill a planter\n",getpid() );
					perror("");
				}
				if(pthread_kill(threads[1], SIGKILL) != 0){
					fprintf(stderr, "Serveur fils %d : pthread_kill a planter\n",getpid() );
					perror("");
				}
				close(descripteurServeurFils);
				close(descripteurClient);
				close(descripteurServeurFilsEtatSites);
				close(descripteurServeurFilsRequetesClient);
				close(descripteurClientEtatSites);
				close(descripteurClientRequetesClient);
				printf("Serveur fils %d : client deconnecter, je termine proprement\n", getpid());
				exit(0);
				// close les IPCS
			}
		}
	}
}

void ecouteClients(int port){
	int descripteurServeur;int descripteurClient;
	struct sockaddr_in addrServeur;
	struct sockaddr_in socketDeUnClient;
	socklen_t tailleSocketAddrIn = sizeof(struct sockaddr_in);
	pid_t pid[NB_CLIENT];
	pid_t pid_c = 0;
	int greetings;
	int i = 0;
	if(routineSocket(&descripteurServeur, &addrServeur, port)){
		fprintf(stderr, "Serveur : erreur routine premiere socket ecouteClient\n");
		exit(1);
	}
	while(1){
		descripteurClient = accept(descripteurServeur, (struct sockaddr*) &socketDeUnClient, &tailleSocketAddrIn);
		if(descripteurClient == -1){
			fprintf(stderr, "Serveur : erreur accept premiere connexion :\n");
			perror("");
		}
		if(recv(descripteurClient, &greetings, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur : erreur recv greetings:\n");
			perror("");
		}
		if(greetings==MAGIC_WORD){
			pid_c = fork();
			if(pid_c == 0){//fils
				traitementClientParUnFils(descripteurClient);
			}else{
				pid[i++] = pid_c;
				if(i >= NB_CLIENT){
					printf("Serveur : capacite de %d clients atteintes\n", NB_CLIENT);
					i = 0;
					while (i < NB_CLIENT) {
						waitpid(pid[i++], NULL, 0);
					}
					i = 0;
				}
			}
		}
	}
}

int main (int argc, char* argv[]){
	if (argc != 3) {
		printf("utilisation: %s <PORT> <CLE>\n", argv[0]);
		exit(1);
	}
	int port = atoi(argv[1]);
	int taille = 10;
	memKey = ftok(argv[2], 418);
	if (memKey == -1) {
		perror("Serveur : erreur ftok");
		exit(1);
	}
	shmId = shmget(memKey,(size_t) sizeof(struct memoirePartager), IPC_CREAT | 0666);
	if (shmId < 0) {
		perror("Serveur : SHARED MEMORY, init\n");
		exit(1);
	}
	printf("Serveur : SHARED MEMORY creer)\n");
	ptrShm = shmat(shmId, NULL, 0);
	if((void*) ptrShm == (void*)-1){
		perror("Serveur : erreur shmat :");
		exit(1);
	}
	ptrShm->taille = taille;
	// I/O sur la SharedMemory
	// { sem0 .. semN } N => nb sites
	semId = semget(memKey, taille, IPC_CREAT | 0666);
	union semun cmdSemInitSites;
	cmdSemInitSites.array = (unsigned short*) malloc(sizeof(unsigned short)*taille);
	if(semId == -1) {
		perror("Serveur : erreur semget");
		exit(1);
	}
	printf("Serveur : Semaphore accede ou cree avec succes :D\n");
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
	initSites(ptrShm->sitesDispo, taille);
	affichageSites(ptrShm->sitesDispo, taille);
	ecouteClients(port);
	return 0;
}
