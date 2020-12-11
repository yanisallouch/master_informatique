#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]) {
	if (argc != 2) {
		printf("utilisation: %s numero_port\n", argv[0]);
		exit(1);
	}

	int ds = socket(PF_INET, SOCK_DGRAM, 0);
	if (ds == -1) {
		perror("Serveur : probleme creation socket\n");
		exit(1);
	}
	printf("ds = %d\n", ds);
	printf("Serveur: creation de la socket : ok\n");

	struct sockaddr_in structServer;
	structServer.sin_family = AF_INET;
	structServer.sin_addr.s_addr = INADDR_ANY;
	structServer.sin_port = htons(atoi(argv[1]));
	printf("Numero de port du serveur (entier) = %d\n", ntohs(structServer.sin_port));
	printf("Numero IP : %d\n", structServer.sin_addr.s_addr);
	if(bind(ds, (struct sockaddr*)&structServer , sizeof(structServer)) < 0) {
		perror("Serveur : erreur bind");
		close(ds);
		exit(1);
	}
	printf("Serveur: nommage : ok\n");
	printf("Numero de port du serveur (entier) = %d\n", ntohs(structServer.sin_port));
	printf("Numero IP : %d\n", structServer.sin_addr.s_addr);

	struct sockaddr_in socketDeUnClient;
	socklen_t tailleSocketAddrIn = sizeof(struct sockaddr_in);

	int retourDeFonction = -42;

	int tailleMessagesRecus = 256;
	char messagesRecus[tailleMessagesRecus];
	int tailleMessagesEnvoyer = 256;
	char *messagesEnvoyer = (char*) malloc (sizeof(char)*tailleMessagesEnvoyer);

	struct pingPong {
		int entier1;
		int size;
		char message[256];
	};
	struct pingPong uneStructureRecue;

	retourDeFonction = recvfrom(ds, &uneStructureRecue, sizeof(uneStructureRecue)+sizeof(char*)*uneStructureRecue.size, 0, (struct sockaddr*)&socketDeUnClient, &tailleSocketAddrIn);
	if(retourDeFonction == -1){
		perror("Serveur: recvfrom a planter");
	}
	printf("Serveur: recvfrom = %d\n", retourDeFonction);
	retourDeFonction = recvfrom(ds, messagesRecus, tailleMessagesRecus*sizeof(char), 0, (struct sockaddr*)&socketDeUnClient, &tailleSocketAddrIn);
	if(retourDeFonction == -1){
		perror("Serveur: recvfrom a planter");
	}
	printf("Serveur: recvfrom = %d\n",retourDeFonction);
	printf("Serveur: entier1 : [%d]\n",uneStructureRecue.entier1);
	printf("Serveur: size : [%d]\n",uneStructureRecue.size);
	printf("Serveur: message : [%s]\n",messagesRecus);
	printf("Serveur: de la part de [IP: %s|PORT: %d]\n", inet_ntoa(socketDeUnClient.sin_addr), ntohs(socketDeUnClient.sin_port) );
	printf("Saisi clavier : ");
	fgets(messagesEnvoyer, tailleMessagesEnvoyer - 1, stdin);
	messagesEnvoyer[strlen(messagesEnvoyer)-1] = '\0';

	retourDeFonction = sendto(ds, messagesEnvoyer, tailleMessagesEnvoyer*sizeof(char), 0, (struct sockaddr*)&socketDeUnClient, tailleSocketAddrIn);
	if(retourDeFonction == -1){
		perror("Serveur: sendto a planter");
	}

	close (ds);
	printf("Serveur : je termine\n");
	exit(1);
}