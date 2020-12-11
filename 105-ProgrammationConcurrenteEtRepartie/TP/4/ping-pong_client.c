#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <string.h>

int main(int argc, char *argv[]) {
	if (argc != 3) {
		printf("utilisation : %s ip_serveur port_serveur\n", argv[0]);
		exit(0);
	}

	int ds = socket(PF_INET, SOCK_DGRAM, 0);
	if (ds == -1) {
	perror("Client : pb creation socket\n");
	exit(1);
	}
	printf("ds = %d\n", ds);
	printf("Client: creation de la socket : ok\n");

	struct sockaddr_in adrServ;
	adrServ.sin_family = AF_INET;
	adrServ.sin_addr.s_addr = inet_pton(AF_INET,argv[1], &(adrServ.sin_addr));
	adrServ.sin_port = htons(atoi(argv[2]));
	printf("Numero de port du serveur (entier) = %d\n", ntohs(adrServ.sin_port));
	printf("Numero IP : %d\n", adrServ.sin_addr.s_addr);

	socklen_t lgAdr = sizeof(struct sockaddr_in);

	int retourDeFonction = -42;

	int tailleMessagesRecus = 256;
	char messagesRecus[tailleMessagesRecus];
	int tailleMessagesEnvoyer = 256;
	char *messagesEnvoyer = (char*) malloc (sizeof(char)*tailleMessagesEnvoyer);

	struct pingPong {
		int entier1;
		int size;
	};
	struct pingPong uneStructureAEnvoyer;

	printf("Saisi clavier : ");
	fgets(messagesEnvoyer, tailleMessagesEnvoyer - 1, stdin);
	messagesEnvoyer[strlen(messagesEnvoyer)-1] = '\0';

	printf("Saisi entier 1 : ");
	scanf("%d", &uneStructureAEnvoyer.entier1);
	printf("%d\n",uneStructureAEnvoyer.entier1 );
	printf("Saisi entier 2 : ");
	scanf("%d", &uneStructureAEnvoyer.size);
	printf("%d\n",uneStructureAEnvoyer.entier1 );

	retourDeFonction = sendto(ds, &uneStructureAEnvoyer, sizeof(uneStructureAEnvoyer), 0, (struct sockaddr*)&adrServ, lgAdr);
	if(retourDeFonction == -1){
		perror("Client: sendto a planter");
	}
	printf("Client: resultat sendto() = %d\n", retourDeFonction );

	retourDeFonction = sendto(ds, messagesEnvoyer, tailleMessagesEnvoyer*sizeof(char), 0, (struct sockaddr*)&adrServ, lgAdr);
	if(retourDeFonction == -1){
		perror("Client: sendto a planter");
	}
	printf("Client: resultat sendto() = %d\n", retourDeFonction );

	retourDeFonction = recvfrom(ds, messagesRecus, tailleMessagesRecus*sizeof(char), 0, (struct sockaddr*)&adrServ, &lgAdr);
	if(retourDeFonction == -1){
		perror("Client: recvfrom a planter");
	}
	printf("Client: reçu [%s]\n",messagesRecus);
	printf("Client: de la part de [IP: %s|PORT: %d]\n",  inet_ntoa(adrServ.sin_addr), ntohs(adrServ.sin_port) );
	close (ds);
	printf("Client : je termine\n");
}
