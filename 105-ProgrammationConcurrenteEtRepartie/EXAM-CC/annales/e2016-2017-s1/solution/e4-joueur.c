#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>


void je_joue() {
    printf("je joue...\n");
    sleep(3);
}


int main(int argc, char* argv[]) {

    if(argc != 4) {
        printf("Utilisation : %s nb_joueurs nb_tours numero_joueur\n", argv[0]);
        return 1;
    }

    // récupération des variables
    int n = atoi(argv[1]);
    int x = atoi(argv[2]);
    int num = atoi(argv[3]);

    // structure des messages échangés
    struct message {
        long etiquette;
    } msg;

    // récupération de la clé
    key_t cle = ftok("/", 0);

    if(cle < 0) {
        perror("erreur ftok");
        return 1;
    }

    // id du tableau de sémaphores
    int idMsg = msgget(cle, 0666);

    if(idMsg < 0) {
        perror("erreur msgget");
        return 1;
    }

    // tours de jeu
    for(int i=1; i<=x; i++) {
        // j'attends mon tour
        printf("j'attends mon tour...\n");
        if(msgrcv(idMsg, &msg, 0, num, 0) < 0) {
            perror("erreur msgrcv");
            exit(1);
        }

        // c'est à moi de jouer
        je_joue();

        // je passe le tour au joueur suivant
        msg.etiquette = num % n + 1;
        printf("je passe le tour au joueur suivant : %ld\n", msg.etiquette);
        if(msgsnd(idMsg, &msg, 0, 0) < 0) {
            perror("erreur msgsnd");
            exit(1);
        }
    }

    // fin
    printf("j'ai fini de jouer\n");

    // signal de la fin du jeu
    if(num == n) {
        msg.etiquette = num + 1;
        if(msgsnd(idMsg, &msg, 0, 0) < 0) {
            perror("erreur msgsnd");
            exit(1);
        }
    }

    return 0;
}
