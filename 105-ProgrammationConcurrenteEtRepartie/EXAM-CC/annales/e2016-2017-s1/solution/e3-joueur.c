#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>


void P(int idSem, unsigned int i, unsigned int k) {
    struct sembuf semoi;
    semoi.sem_num = i;
    semoi.sem_op = -k;
    semoi.sem_flg = 0;
    semop(idSem, &semoi, 1);
}

void V(int idSem, unsigned int i, unsigned int k) {
    struct sembuf semoi;
    semoi.sem_num = i;
    semoi.sem_op = k;
    semoi.sem_flg = 0;
    semop(idSem, &semoi, 1);
}

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
    key_t cle = ftok("/", 0);

    if(cle < 0) {
        perror("erreur ftok");
        return 1;
    }

    // id du tableau de sémaphores
    int idSem = semget(cle, 1, 0666);

    if(idSem < 0) {
        perror("erreur semget");
        return 1;
    }

    // tours de jeu
    for(int i=1; i<=x; i++) {
        // j'attends mon tour
        printf("j'attends mon tour...\n");
        int tour = n * (i-1) + num;
        P(idSem, 0, tour);

        // c'est à moi de jouer
        je_joue();

        // je passe le tour au joueur suivant
        printf("je passe le tour au joueur suivant\n");
        V(idSem, 0, tour + 1);
    }

    printf("j'ai fini de jouer\n");

    return 0;
}
