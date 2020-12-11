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

// Variable Globale
int idSem;
int idSemProcesseurs;
int idSemStockages;
int shId;
struct memoirePartager * ptrShm;

struct ressources {
	char label[255];
	int nbProcesseur;//les ressources libres
	int nbCapaciteStockage;//les ressources libres
	int nbProcesseurPartage; // initialise a nbProc
	int nbCapaciteStockagePartage; // init ...
};

struct memoirePartager {
	int taille;
	struct ressources ressourcesDispo[1000];
	// 1000 slots differents
};

struct requeteReservation {
	int label;
	int nbProcesseur;
	int nbCapaciteStockage;
	int typePartage; //0 exclusif 1 partage
	int typeReservation; //0 demande 1 liberation
};

struct paramThreadType1 {
	int * descripteurSocket;
};

union semun {
	int              val;    /* Valeur pour SETVAL */
	struct semid_ds *buf;    /* Tampon pour IPC_STAT, IPC_SET */
	unsigned short  *array;  /* Tableau pour GETALL, SETALL */
	struct seminfo  *__buf;  /* Tampon pour IPC_INFO (specifique à Linux) */
};

void initRessources(struct ressources * ressourcesDispo, int taille){
	char nomVilles[1000][255] =  {"Montpellier","Toulouse","Lyon","Marseille","Nice","Nantes","Strasbourg","Bordeaux","Lille","Rennes"};
	for(int i=0;i<taille;i++){
		strcpy(ressourcesDispo[i].label, nomVilles[i]);
	}

	int nbProcesseurs[1000]={240,140,380,190,290,310,100,90,107,25};
	for(int i=0;i<taille;i++){
		ressourcesDispo[i].nbProcesseur = nbProcesseurs[i];
	}
	int nbCapaciteStockage[1000]={1500,800,900,3000,780,4000,2700,800,1700,400};

	for(int i=0;i<taille;i++){
		ressourcesDispo[i].nbCapaciteStockage = nbCapaciteStockage[i];
	}

	for (size_t i = 0; i < (size_t)taille; i++) {
		ressourcesDispo[i].nbProcesseurPartage = 0;
		ressourcesDispo[i].nbCapaciteStockagePartage = 0;
	}
	// partie semaphore
	// remarque on pourrait recuperer la valeur dejà initialiser au meme indice...
	// sem_init(idSemProrcesseurs[i])
	union semun cmdSemProcesseurs;
	cmdSemProcesseurs.array = (unsigned short*)malloc(sizeof(unsigned short)*taille);
	union semun cmdSemStockages;
	cmdSemStockages.array = (unsigned short*)malloc(sizeof(unsigned short)*taille);
	for (size_t i = 0; i < (size_t) taille; i++) {
		cmdSemProcesseurs.array[i] = ressourcesDispo[i].nbProcesseur;
		cmdSemStockages.array[i] = ressourcesDispo[i].nbCapaciteStockage;
	}
	if(semctl(idSemProcesseurs, 0, SETALL, cmdSemProcesseurs) == -1){
		perror("Serveur : erreur semctl init processeurs");
		exit(1);
	}
	if(semctl(idSemStockages, 0, SETALL, cmdSemStockages) == -1){
		perror("Serveur : erreur semctl init stockages");
		exit(1);
	}
}

void affichageRessources(struct ressources * ressourcesDispo, int taille){
	system("clear");
	printf("%-15s %-11s %-11s\n", "Label", "Nb-CPU", "Stockage (Go)" );
	printf("-------------------------------------------\n");
	for (size_t i = 0; i < (size_t)taille; i++) {
		printf("%-15s %-11d %-11d\n", ressourcesDispo[i].label, ressourcesDispo[i].nbProcesseur, ressourcesDispo[i].nbCapaciteStockage );
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

int P(int idSem, int idVille, int nbRessources){
	struct sembuf *sopsP = (struct sembuf*) malloc(sizeof(struct sembuf));
	sopsP->sem_num = idVille;
	sopsP->sem_op = -nbRessources;
	sopsP->sem_flg = IPC_NOWAIT | SEM_UNDO;
	if(semop(idSem, sopsP, 1)){
		fprintf(stderr, "Serveur fils %d : erreur semop P\n", getpid());
		perror("");
		return 1;
	}
	return 0;
}

int V(int idSem, int idVille, int nbRessources){
	struct sembuf *sopsV = (struct sembuf*) malloc(sizeof(struct sembuf));
	sopsV->sem_num = idVille;
	sopsV->sem_op = +nbRessources;
	sopsV->sem_flg = IPC_NOWAIT | SEM_UNDO;
	if(semop(idSem, sopsV, 1)){
		fprintf(stderr, "Serveur fils %d : erreur semop P\n", getpid());
		perror("");
		return 1;
	}
	return 0;
}

void *fnt_eTatRessources(void * param){
	struct paramThreadType1 *paramThread = (struct paramThreadType1 *) param;
	int taille = ptrShm->taille;
	struct ressources copieRessourcesDisponibles[1000];
	struct sembuf * opp = (struct sembuf *) malloc(sizeof(struct sembuf));
	struct sembuf * opv = (struct sembuf *) malloc(sizeof(struct sembuf));
	opp->sem_num=0;
	opp->sem_op=-1;
	opp->sem_flg=SEM_UNDO;
	opv->sem_num=0;
	opv->sem_op=+1;
	opv->sem_flg=SEM_UNDO;
	if(semop(idSem,opp,1) == -1){
		fprintf(stderr, "Serveur fils %d : erreur semop prise I/O premiere envois\n", getpid());
		perror("");
	}
	for (int i = 0; i < taille; ++i){
		copieRessourcesDisponibles[i] = ptrShm->ressourcesDispo[i];
	}
	if (semop(idSem,opv,1) == -1){
		fprintf(stderr, "Serveur fils %d : erreur semop liberation I/O premiere envois\n", getpid());
		perror("");
	}
	if(send(*(paramThread->descripteurSocket), &taille, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send taille ressourcesDispo :\n", getpid());
		perror("");
	}
	if(send(*(paramThread->descripteurSocket), &copieRessourcesDisponibles, sizeof(struct ressources)*taille, 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send ressourcesDispo[*] :\n", getpid());
		perror("");
	}
	int indiceModifier[taille];
	memset(indiceModifier, 0, taille*sizeof(int));
	int tailleIndiceModifier = 0;
	int nbDeVariableModifier = 0;
	// tableau des indices modifier, valeur possible {0,1}
	while(1){
		sleep(1);
		if(semop(idSem,opp,1) == -1){
			fprintf(stderr, "Serveur fils %d : erreur semop prise I/O boucleEtatRessources\n", getpid());
			perror("");
		}
		for (int i = 0; i < taille; ++i){
			if(copieRessourcesDisponibles[i].nbProcesseur != ptrShm->ressourcesDispo[i].nbProcesseur){
				// pour l'instant on a 1 label unique
				copieRessourcesDisponibles[i].nbProcesseur = ptrShm->ressourcesDispo[i].nbProcesseur;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if(copieRessourcesDisponibles[i].nbCapaciteStockage != ptrShm->ressourcesDispo[i].nbCapaciteStockage){
				// pour l'instant on a 1 label unique
				copieRessourcesDisponibles[i].nbCapaciteStockage = ptrShm->ressourcesDispo[i].nbCapaciteStockage;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if(copieRessourcesDisponibles[i].nbProcesseurPartage != ptrShm->ressourcesDispo[i].nbProcesseurPartage){
				// pour l'instant on a 1 label unique
				copieRessourcesDisponibles[i].nbProcesseurPartage = ptrShm->ressourcesDispo[i].nbProcesseurPartage;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if(copieRessourcesDisponibles[i].nbCapaciteStockagePartage != ptrShm->ressourcesDispo[i].nbCapaciteStockagePartage){
				// pour l'instant on a 1 label unique
				copieRessourcesDisponibles[i].nbCapaciteStockagePartage = ptrShm->ressourcesDispo[i].nbCapaciteStockagePartage;
				indiceModifier[i] = 1;
				nbDeVariableModifier++;
			}
			if (nbDeVariableModifier > 0){
				// on a eu 1 modification au moins alors
				tailleIndiceModifier++;
			}

			if(send(*(paramThread->descripteurSocket), &tailleIndiceModifier, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur fils %d : erreur send tailleDesRessourcesModifier :\n", getpid());
				perror("");
			}
			// indiceModifier contient le flag
			// tailleIndiceModifier ==  combien de flag on doit parcourir
			// copieRessourcesDisponibles ==  au information a parcourir selon l'indiceModifier
			for (size_t i = 0; i < (size_t)taille; i++) {
				if(indiceModifier[i] == 1){
					// on a un flag pour send copieRessource[i]
					if(send(*(paramThread->descripteurSocket), &i, sizeof(int), 0) == -1){
						fprintf(stderr, "Serveur fils %d : erreur send  indice <%d> de la ressource a modifier :\n", getpid(), (int)i);
					}
					if(send(*(paramThread->descripteurSocket), &copieRessourcesDisponibles[i], sizeof(struct ressources), 0) == -1){
						fprintf(stderr, "Serveur fils %d : erreur send <%d>eme valeur de resourceDispo <%s | %d | %d>:\n", getpid(),(int)i, copieRessourcesDisponibles[i].label,copieRessourcesDisponibles[i].nbProcesseur,copieRessourcesDisponibles[i].nbCapaciteStockage);
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
	struct paramThreadType1 *paramThread = (struct paramThreadType1 *) param;
	// int taille = ptrShm->taille;
	struct sembuf * opp = (struct sembuf *) malloc(sizeof(struct sembuf));
	struct sembuf * opv = (struct sembuf *) malloc(sizeof(struct sembuf));
	opp->sem_num=0;
	opp->sem_op=-1;
	opp->sem_flg=SEM_UNDO;
	opv->sem_num=0;
	opv->sem_op=+1;
	opv->sem_flg=SEM_UNDO;
	int idVille = 0;
	int nbProcesseur = 1;
	int nbStockage = 1;
	int flagPartager = 0;
	// int priseEnCharge = 666;

// TODO HISTORIQUE DES REQUETES

	while(1){
		// TODO CRC ou structure
		if(recv(*(paramThread->descripteurSocket), &idVille, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient idVille :\n",getpid());
			perror("");
		}
		if(recv(*(paramThread->descripteurSocket), &nbProcesseur, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient nbProc:\n", getpid());
			perror("");
		}
		if(recv(*(paramThread->descripteurSocket), &nbStockage, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient nbStockage:\n", getpid());
			perror("");
		}
		if(recv(*(paramThread->descripteurSocket), &flagPartager, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur fils %d : erreur recv requeteClient flagPartager:\n", getpid());
			perror("");
		}
		// TODO send prise en charge +
		// if(send(*(paramThread->descripteurSocket),&priseEnCharge,sizeof(int),0) == -1){
		// 	fprintf(stderr, "Serveur fils %d : erreur send requeteClient priseEnCharge:\n", getpid());
		// 	perror("");
		// }
		// if(semop(idSem,opp,1) == -1){
		// 	fprintf(stderr, "Serveur fils %d : erreur semop requeteClient PriseSem I/O\n", getpid());
		// 	perror("");
		// }
		// 	// accepter/refuser apres prise de semaphore + historique des reservations
		// if(flagPartager){
		// 	//partage
		// 	// TODO
		// 	// 250 total
		// 	// c1 200e
		// 	// c2 50p
		// 	// c3 25e
		// 	// C1 200e ==> P(200)
		// 	// C2  demande p ==>
		// 	// if nbProc-nbProcesseurPartage>0(  P(nbProc -nbProcesseurPartage) && {nbProcesseurPartage+=nbProc;}
		// 	//exclusif
		// 	//P(idSemProcesseurs, idVille,nbProcesseur-nbProcesseurPartage);
		// 	//P(idSemStockages, idVille, );
		// }
		// else{
		// 	//exclusif
		// 	if(P(idSemProcesseurs, idVille, nbProcesseur) == 1){
		// 		fprintf(stderr, "Serveur fils %d : erreur requeteClient PriseSem  idSemProcesseur\n", getpid());
		// 	}
		// 	if(P(idSemStockages, idVille, nbStockage) == 1){
		// 		fprintf(stderr, "Serveur fils %d : erreur requeteClient PriseSem  idSemStockages\n", getpid());
		// 		if(V(idSemStockages, idVille, nbStockage) == 1){
		// 			fprintf(stderr, "Serveur fils %d : erreur requeteClient Verhogen idSemStockages\n", getpid());
		// 		}
		// 	}
		// }
		// if (semop(idSem,opv,1) == -1){
		// 	fprintf(stderr, "Serveur fils %d : erreur semop requeteClient  LibSem I/O", getpid());
		// 	perror("");
		// }
		// if(send(*(paramThread->descripteurSocket), &taille, sizeof(int), 0) == -1){
		// 	fprintf(stderr, "Serveur fils %d : erreur send  requeteClient taille-ressourcesDispo :\n", getpid());
		// 	perror("");
		// }
	}

	pthread_exit(NULL);
}

void traitementClientParUnFils(int descripteurServeurFils){
	struct sockaddr_in socketClientDuThread;
	int port;
	socklen_t tailleSocketAddrIn = sizeof(struct sockaddr_in);
	int descripteurClient = accept(descripteurServeurFils, (struct sockaddr*) &socketClientDuThread, &tailleSocketAddrIn);
	if(descripteurClient == -1){
		fprintf(stderr, "Serveur fils %d : erreur accept connection socket client :\n", getpid());
		perror("");
	}
	//le client est conneter, je lui envois le port dedie pour le thread etatRessources

	int descripteurServeurFilsEtatRessources;
	struct sockaddr_in addrServeurFilsEtatRessources;
	if(routineSocket(&descripteurServeurFilsEtatRessources, &addrServeurFilsEtatRessources, socketClientDuThread.sin_port) == -1){
		fprintf(stderr, "Serveur fils %d: erreur traitementClientParUnFils socket etatRessource serveur fils\n", getpid());
		exit(1);
		// gros doute sur le comportement de l'arret =>> le fork ou le parent avec lui
	}
	port = ntohs(addrServeurFilsEtatRessources.sin_port);
	if(send(descripteurClient, &port, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send port addrServeurFilsEtatRessources : \n", getpid());
		perror("");
	}
	struct sockaddr_in addrClientDuThreadEtatRessources;
	int descripteurClientEtatRessources = accept(descripteurServeurFilsEtatRessources, (struct sockaddr*) &addrClientDuThreadEtatRessources, &tailleSocketAddrIn);
	if(descripteurClientEtatRessources == -1){
		fprintf(stderr, "Serveur fils %d : erreur accept connection socket client etatRessource :\n", getpid());
		perror("");
		close(descripteurServeurFils);
		exit(1);
	}
	//le client est conneter, je lui envois le port dedier pour le thread etatRessources utilise pour la socket du thread requeteClient
	int descripteurServeurFilsRequetesClient;
	struct sockaddr_in addrServeurFilsRequetesClient;
	if(routineSocket(&descripteurServeurFilsRequetesClient, &addrServeurFilsRequetesClient, 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur traitementClientParUnFils socket requeteClient serveur fils\n", getpid());
		exit(1);
	}
	port = ntohs(addrServeurFilsRequetesClient.sin_port);
	if(send(descripteurClient, &port, sizeof(int), 0) == -1){
		fprintf(stderr, "Serveur fils %d : erreur send port addrServeurFilsRequetesClient : \n", getpid());
		perror("");
	}
	struct sockaddr_in addrClientDuThreadRequetesClient;
	int descripteurClientRequetesClient = accept(descripteurServeurFilsRequetesClient, (struct sockaddr*) &addrClientDuThreadRequetesClient, &tailleSocketAddrIn);
	if(descripteurClientRequetesClient == -1){
		fprintf(stderr, "Serveur fils %d : erreur accept connection socket client requeteClient :\n", getpid());
		perror("");
		close(descripteurServeurFils);
		exit(1);
	}
	int nbThreads = 2;
	pthread_t * threads =(pthread_t*)malloc(nbThreads*sizeof(pthread_t));
	struct paramThreadType1 *paramEtatRessources = (struct paramThreadType1*) malloc(sizeof(struct paramThreadType1));
	paramEtatRessources->descripteurSocket=&descripteurClientEtatRessources;
	if (pthread_create(&threads[0],NULL,fnt_eTatRessources,paramEtatRessources) != 0) {
		fprintf(stderr, "Serveur fils %d : erreur creation thread etatRessource", getpid());
		perror("");
		exit(1);
	}
	struct paramThreadType1 *paramRequetes = (struct paramThreadType1*) malloc(sizeof(struct paramThreadType1));
	paramRequetes->descripteurSocket=&descripteurClientRequetesClient;
	if (pthread_create(&threads[1],NULL,fnt_requeteCliente,paramRequetes) != 0) {
		fprintf(stderr, "Serveur fils %d : erreur creation thread requeteClient", getpid());
		perror("");
		exit(1);
	}
}

void ecouteClient(int port){
	int descripteurServeur;
	struct sockaddr_in addrServeur;
	if(routineSocket(&descripteurServeur, &addrServeur, port)){
		fprintf(stderr, "Serveur : erreur routine premiere socket ecouteClient\n");
		exit(1);
	}
	while(1){
		struct sockaddr_in socketDeUnClient;
		socklen_t tailleSocketAddrIn = sizeof(struct sockaddr_in);

		int descripteurClient = accept(descripteurServeur, (struct sockaddr*) &socketDeUnClient, &tailleSocketAddrIn);

		if(descripteurClient == -1){
			fprintf(stderr, "Serveur : erreur accept premiere connexion :\n");
			perror("");
		}
		int greetings;
		if(recv(descripteurClient, &greetings, sizeof(int), 0) == -1){
			fprintf(stderr, "Serveur : erreur recv greetings:\n");
			perror("");
		}
		if(greetings==408){
			int descripteurServeurFils;
			struct sockaddr_in addrServeurFils;
			if(routineSocket(&descripteurServeurFils, &addrServeurFils, 0)){
				fprintf(stderr, "Serveur : erreur routine socket dedie serveur fils ecouteClient\n");
				exit(1);
			}
			port = ntohs(addrServeurFils.sin_port);
			if(send(descripteurClient, &port, sizeof(int), 0) == -1){
				fprintf(stderr, "Serveur : erreur send port serveur fils :\n");
				perror("");
			}
			int pid = fork();
			if(!pid){//fils
				traitementClientParUnFils(descripteurServeurFils);
			}
		}
		close(descripteurClient);
	}
}

int main (int argc, char* argv[]){
	if (argc != 3) {
		printf("utilisation: %s <PORT> <CLE>\n", argv[0]);
		exit(1);
	}

	key_t cle = ftok(argv[2], 418);
	if (cle == -1) {
		perror("Serveur : erreur ftok");
		exit(1);
	}
	// I/O sur la SharedMemory
	// { sem0 }
	idSem = semget(cle, 1, IPC_CREAT | 0666);
	if(idSem == -1) {
		perror("Serveur : erreur semget");
		exit(1);
	}
	printf("Serveur : Semaphore accede ou cree avec succes :D\n");
	// { semProc0 ... semProcN}
	idSemProcesseurs = semget(cle+1, 10, IPC_CREAT | 0666);
	if(idSemProcesseurs == -1) {
		perror("Serveur : erreur semget");
		exit(1);
	}
	printf("Serveur : Semaphore accede ou cree avec succes :D\n");
	// { semStockages0 ... semStockagesN}
	idSemStockages = semget(cle+2, 10, IPC_CREAT | 0666);
	if(idSemStockages == -1) {
		perror("Serveur : erreur semget");
		exit(1);
	}
	printf("Serveur : Semaphore accede ou cree avec succes :D\n");

	union semun egCtrl;
	egCtrl.val=1;
	if(semctl(idSem, 0, SETVAL, egCtrl) == -1){
		perror("Serveur : erreur semctl init");
		exit(1);
	}

	printf("Serveur : Ensemble de Semaphore initialise\n");

	shId = shmget(cle, (1*sizeof(struct ressources*)), IPC_CREAT|0666);
	if(shId == -1){
		perror("Serveur : erreur shmget :");
		exit(1);
	}
	printf("Serveur : SHARED MEMORY, accede ou cree avec succes :D\n");

	ptrShm = shmat(shId, NULL, 0);
	if((void*) ptrShm == (void*)-1){
		perror("Serveur : erreur shmat :");
		exit(1);
	}

	int taille = 10;
	ptrShm->taille = taille;
	struct ressources * ressourcesDispo = ptrShm->ressourcesDispo;
	ressourcesDispo = (struct ressources*) malloc(sizeof(struct ressources) * taille);
	initRessources(ressourcesDispo, taille);
	affichageRessources(ressourcesDispo, taille);
	int port = atoi(argv[1]);
	ecouteClient(port);

//protocole :
//client envoi un message initial au serveur
//le serveur repond un id de file ou de socket
//initialise un thread dans le serveur
//thread ecoute sur cet objet
//client communique via son objet dedier dans un 1er thread
//envoyer une struture requeteReservation
//dans un 2eme thread boucle infi avec un sleep
//demander l'etat actuel et maj local

//initialisation du port d'ecoute du serveur/file de message
//attente connection
//fork
//traitement client

	return 0;
}
