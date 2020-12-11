#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>


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

    // création sémaphore
    int sem = semget(cle, 1, IPC_CREAT|0666);

    if(sem < 0) {
        perror("erreur semget");
        return 1;
    }

    // init du tableau
    union semun {
        int val;
    } arg;
    arg.val = 1;

    if(semctl(sem, 0, SETVAL, arg) < 0) {
        perror("erreur semctl");
        if(semctl(sem, 0, IPC_RMID) < 0) {
            perror("erreur rm sem");
        }
        return 1;
    }

    // attente de la fin du jeu
    struct sembuf buf;
    buf.sem_num = 0;
    buf.sem_op = -(n * x + 1); // après le x-eme tour du n-eme joueur
    buf.sem_flg = 0;

    if(semop(sem, &buf, 1) < 0) {
        perror("erreur semop");
    }

    printf("fin du jeu !\n");

    // destruction du tableau de sémaphores
    if(semctl(sem, 0, IPC_RMID) < 0) {
        perror("erreur rm sem");
        return 1;
    }
}
