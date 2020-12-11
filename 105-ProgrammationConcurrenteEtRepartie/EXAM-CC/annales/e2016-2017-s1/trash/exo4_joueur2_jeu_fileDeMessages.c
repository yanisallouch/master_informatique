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
	if(argc != 2){
		printf("usage:\n\t./%s nbJoueur\n", argv[0]);
		exit(1);
	}

	int i = 2; // numero du joueur

	struct my_msgbuf buf;
	int msqid;
	int len;
	key_t key;
	system("touch msgExo4.txt");

	if ((key = ftok("msgExo4.txt", 'B')) == -1) {
		perror("Joueur : ftok");
		exit(1);
	}

	if ((msqid = msgget(key, PERMS | IPC_CREAT)) == -1) {
		perror("Joueur : msgget");
		exit(1);
	}
	printf("Joueur : message queue: ready to send messages.\n");

	while(1){
		len = sizeof(buf);
		if (msgrcv(msqid, &buf, len, i-1, 0) == -1) {
			perror("msgrcv");
			exit(1);
		}else{
			printf("Joueur : Joueur %d/%d a reçu le message du joueur %d\n", i, atoi(argv[1]), buf.mtype);
			if(i ==  atoi(argv[1])){
				printf("Joueur : tour %d de jeu fini par le joueur %d.\n", buf.cptTour, i);
				buf.cptTour++;
				buf.mtype = -1; // la boucle est bouclé
			}else{
				buf.mtype = i;
			}
			if (msgsnd(msqid, &buf, len, 0) == -1){
				perror("Joueur : msgsnd");
			}
			printf("Joueur : Joueur %d a deposer un message pour le %d\n", i, i+1);
			buf.mtype = 99; // je notifie le maitre du jeu
			if (msgsnd(msqid, &buf, len, 0) == -1){
				perror("Joueur : msgsnd");
			}
			printf("MJ notifie\n");
		}
		sleep(3);
	}


	len = sizeof(buf);
	if (msgsnd(msqid, &buf, len, 0) == -1){
		perror("Joueur : msgsnd");
	}

	if (msgctl(msqid, IPC_RMID, NULL) == -1) {
		perror("Joueur : msgctl");
		exit(1);
	}
	printf("Joueur : message queue done sending messages.\n");
	return 0;
}