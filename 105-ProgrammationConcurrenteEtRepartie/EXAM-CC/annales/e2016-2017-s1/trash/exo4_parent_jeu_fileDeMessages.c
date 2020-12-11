#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#define PERMS 0644
struct my_msgbuf {
	int mtype;
	int cptTour;
};

int main(int argc, char ** argv) {
	if(argc != 3){
		printf("usage:\n\t./%s nbJoueur nbDeTour\n", argv[0]);
		exit(1);
	}

	struct my_msgbuf buf;
	int msqid;
	int len;
	key_t key;
	system("touch msgExo4.txt");

	if ((key = ftok("msgExo4.txt", 'B')) == -1) {
		perror("Maitre du jeu : ftok");
		exit(1);
	}

	if ((msqid = msgget(key, PERMS | IPC_CREAT)) == -1) {
		perror("Maitre du jeu : msgget");
		exit(1);
	}
	printf("Maitre du jeu : message queue: ready to send messages.\n");
	buf.mtype = -1; // init pour le joueur 0
	buf.cptTour = 0;



	while(1) {
		len = sizeof(buf);
		if (msgsnd(msqid, &buf, len, 0) == -1){
			perror("Maitre du jeu : msgsnd");
		}
		if (msgrcv(msqid, &buf, len, 99, 0) == -1) {
			perror("msgrcv");
			exit(1);
		}
		if(buf.mtype >= 0 && buf.mtype < atoi(argv[1])) {
			printf("Maitre du jeu : le joueur %d/%d à joué son %d/%d tour\n", buf.mtype, atoi(argv[1]), buf.cptTour, atoi(argv[2]));
		}

		if (buf.mtype == -2) {
		// dernier joueur a joué le dernier tour de jeu
			printf("Maitre du jeu : le joueur %d à joué son dernier coup du dernier %dieme tour\n", buf.mtype, buf.cptTour);
			break;
		}
		sleep(3);
	}

	len = sizeof(buf);
	if (msgsnd(msqid, &buf, len, 0) == -1){
		perror("Maitre du jeu : msgsnd");
	}

	if (msgctl(msqid, IPC_RMID, NULL) == -1) {
		perror("Maitre du jeu : msgctl");
		exit(1);
	}
	printf("Maitre du jeu : message queue done sending messages.\n");
	return 0;
}