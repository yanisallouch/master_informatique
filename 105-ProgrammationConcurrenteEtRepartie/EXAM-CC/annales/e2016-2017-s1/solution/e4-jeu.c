#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>


int main() {
    int n, x;

    do {
        printf("Saisir le nombre de joueurs : ");
        scanf("%d", &n);
    } while(n < 1);

    do {
        printf("Saisir le nombre de tours : ");
        scanf("%d", &x);
    } while(x < 1);

    // récupération de la clé
    key_t cle = ftok("/", 0);

    if(cle < 0) {
        perror("erreur ftok");
        return 1;
    }

    // création file de messages
    int idMsg = msgget(cle, IPC_CREAT|0666);

    if(idMsg < 0) {
        perror("erreur msgget");
        return 1;
    }

    // début du jeu
    struct message {
        long etiquette;
    } msg;

    msg.etiquette = 1;

    if(msgsnd(idMsg, &msg, 0, 0) < 0) {
        perror("erreur msgsnd");
    }

    // attente de la fin du jeu
    if(msgrcv(idMsg, &msg, 0, n + 1, 0) < 0) {
        perror("erreur msgrcv");
    }

    printf("fin du jeu !\n");

    // destruction du tableau de sémaphores
    if(msgctl(idMsg, IPC_RMID, NULL) < 0) {
        perror("erreur rm msg");
        return 1;
    }

    return 0;
}
