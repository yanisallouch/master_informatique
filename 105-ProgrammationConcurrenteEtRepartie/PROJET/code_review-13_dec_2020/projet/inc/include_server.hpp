#include <iostream>
#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h> // htons
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "../inc/site.hpp"
#include "../inc/cloud.hpp"
#include "../inc/reservation.hpp"
#include "../inc/define.hpp"
#include "../inc/protocol.hpp"

using namespace std;

//------ Variables globales du serveur
int semid;
int shmid;
char *cloud_json;
Cloud *cloud;
Cloud *cloud_initial;
Reservation *reservation;

int* all_client_ds;
int ds_server_main, id;
int parent_pid;

/* Varibales partagé entre les threads de chaque processus */
int* n_threads_concurant;
int* n_threads_courant;

int shm_concurant;
int shm_courant;

struct sembuf verrouP = {.sem_num = 0, .sem_op = -1, .sem_flg = 0};
struct sembuf verrouV = {.sem_num = 0, .sem_op = 1, .sem_flg = 0};
struct sembuf signalP = {.sem_num = 1, .sem_op = -1, .sem_flg = 0};
struct sembuf signalV = {.sem_num = 1, .sem_op = 1, .sem_flg = 0};
struct sembuf signalZ = {.sem_num = 1, .sem_op = 0, .sem_flg = 0};

struct sembuf rendezvousP = {.sem_num = 2, .sem_op = -1, .sem_flg = 0};
struct sembuf rendezvousV = {.sem_num = 2, .sem_op = 1, .sem_flg = 0};

struct sembuf signalRdP = {.sem_num = 3, .sem_op = -1, .sem_flg = 0};
struct sembuf signalRdV = {.sem_num = 3, .sem_op = 1, .sem_flg = 0};
struct sembuf signalRdZ = {.sem_num = 3, .sem_op = 0, .sem_flg = 0};


char *execute_cmd(commande* cmd, Reservation *reservation, char* res, int ds_client)
{
    strcpy(res, "");
    if (cmd->cmd_type == CMD_ALLOC_WITH_NAMES)
    {
        semop(semid, &verrouP, 1);
        int waiting = 0;
        destroy_cloud(cloud);
        cloud = decode_cloud(cloud_json, MAX_LEN_BUFFER_JSON);
        int check;
        while ((check = check_commande(cloud, cmd,cloud_initial)) == -1)
        {
            cout << "mise en attente" << endl;
            semop(semid, &verrouV, 1);
            if ((waiting == 0) && (sendTCP(ds_client, "wait", strlen("wait")) <= 0)) {
                cerr << "Error snd" << endl;
                return res;
            } else
            waiting = 1;
            semop(semid, &signalZ, 1);
            semop(semid, &verrouP, 1);
            destroy_cloud(cloud);
            cloud = decode_cloud(cloud_json, MAX_LEN_BUFFER_JSON);
        }
        if (check == -2) {
            strcpy(res,"Can't alloc");
            semop(semid, &verrouV,1);
            return res;
        }
        if ((waiting == 1) && (sendTCP(ds_client, "stopwait", strlen("stopwait")) <= 0)) {
            cerr << "Error snd" << endl;
            semop(semid, &verrouV,1);
            return res;
        }

        for (int i = 0; i < cmd->nb_server; i++)
        {
            if (reserve_resources(cloud, reservation, cmd->server_name[i], {.cpu = cmd->cpu[i], .memory = cmd->memory[i]}) == -1)
            {
                cerr << "Impossible d'allouer " << cmd->server_name[i] << endl;
            }
        }
        // envoi du signal de modification de cloud
        code_cloud(cloud, cloud_json, MAX_LEN_BUFFER_JSON);
        semop(semid, &verrouV, 1);
        semop(semid, &signalP, 1);
        strcpy(res, "commande d'allocation acceptée");
    }
    else if (cmd->cmd_type == CMD_ALLOC_ALL)
    {
        cout << "CMD_ALLOC_ALL" << endl;
        cout << "non implémenté" << endl;
        strcpy(res, "non implémenter");
    }
    else if (cmd->cmd_type == CMD_FREE_WITH_NAMES)
    {
        semop(semid, &verrouP, 1);
        for (int i = 0; i < cmd->nb_server; i++)
        {
            free_allocation(cloud, reservation, cmd->server_name[i]);
        }
        code_cloud(cloud, cloud_json, MAX_LEN_BUFFER_JSON);
        // envoi du signal de modification de cloud
        semop(semid, &verrouV, 1);
        semop(semid, &signalP, 1);
        strcpy(res, "commande de free accepte");
    }
    else if (cmd->cmd_type == CMD_EXIT || cmd->cmd_type == CMD_FREE_ALL)
    {
        semop(semid, &verrouP, 1);
        if (free_all_allocation(cloud, reservation) == -1)
        {
            strcpy(res,"Free impossible");
            return res;
        }
        else
        {
            if (code_cloud(cloud, cloud_json, MAX_LEN_BUFFER_JSON) == -1)
            {
                cerr << "impossible de coder le cloud en json" << endl;
            }
        }
        semop(semid, &verrouV, 1);
        semop(semid, &signalP, 1);

        strcpy(res, "free all ok");
    }
    return res;
}

/* premier thread attends une modification pour afficher */
void *wait_thread(void *params)
{
    struct recv_msg *r = (struct recv_msg *)params;
    int s = pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
    if (s != 0)
        cerr << "Can't set cancel state" << endl;
    s = pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, NULL);

    while (1)
    {
        semop(semid, &signalZ, 1);
        semop(semid, &verrouP, 1);

        if (cloud_json != NULL && strlen(cloud_json) > 0 && cloud_json[0] == '[' && cloud_json[strlen(cloud_json)-1] == ']') {

            cloud = decode_cloud(cloud_json, MAX_LEN_BUFFER_JSON);
            if (cloud == NULL) {
                cerr << "Impossible de decoder le cloud" << endl;
                return NULL;
            }
            if (r != NULL) {
                strcpy(r->msg, cloud_json);
                if ((sendTCP(r->ds_client, r->msg, strlen(r->msg) + 1) == -1)) {
                    perror("Can't send the message:");
                    break;
                }
                print_reservation(reservation,r->msg);
                if ((sendTCP(r->ds_client, r->msg, strlen(r->msg) + 1) == -1)) {
                    perror("Can't send the message:");
                    break;
                }
            }
        } else {
            cerr << "Réception d'un cloud_json vide : " << (cloud_json==NULL?"null":"notnull") << endl;
        }
        semop(semid, &verrouV, 1);

        semop(semid, &rendezvousP, 1);
        (*n_threads_courant) += 1;
        if ((*n_threads_courant) >= (*n_threads_concurant)) {
            semop(semid, &signalV, 1);
            (*n_threads_courant) = 0;
            semop(semid,&signalRdP,1);
            semop(semid,&signalRdV,1);
            semop(semid, &rendezvousV, 1);
        }
        else
        {
            semop(semid, &rendezvousV, 1);
            semop(semid,&signalRdZ,1);
        }
    }
    semop(semid, &verrouV, 1);
    return NULL;
}

int manage_user(int ds_client)
{

    Client client;
    client.id = id;

    semop(semid, &verrouP, 1);
    cloud_json = (char *)shmat(shmid, NULL, 0);
    if (cloud_json == (void *)-1)
    {
        perror("erreur shmat");
        exit(1);
    }
    cloud = decode_cloud(cloud_json, MAX_LEN_BUFFER_JSON);
    if (cloud == NULL)
    {
        cerr << "cloud NULL" << endl;
    }
    reservation = init_reservation(cloud, client);
    semop(semid, &signalP, 1);
    semop(semid, &verrouV, 1);

    fflush(stdin);
    commande* cmd = init_commande(cloud);
    if (cmd == NULL) {cerr<<"Can't init commande"<<endl;return 1;}
    while (1)
    {
        char in_buffer[100];
        int rcv = recvCommandeTCP(ds_client, cmd);
        if (rcv == -1){perror("Error recv:");break;}
        execute_cmd(cmd, reservation, in_buffer, ds_client);

        if (strcmp(in_buffer, "") == 0)
            strcpy(in_buffer, "Erreur à l'éxecution de la commande");
        if (cmd->cmd_type == CMD_EXIT)
            break;
        semop(semid, &verrouP, 1);
        if (sendTCP(ds_client, in_buffer, strlen(in_buffer)) == -1) {
            perror("Can't send the message:");
            semop(semid, &verrouV, 1);
            return -1;
        }
        semop(semid, &verrouV, 1);

    }
    return 0;
}

void server_signal_handler(int sig) {
    if (sig == SIGINT && getpid() == parent_pid) {
        cout << endl << "SIGINT received" << endl;
        semop(shmid, &signalP, 1);
        if (free_all_allocation(cloud,reservation) == -1)
            cerr << "Can't free all allocation" << endl;
        else
            cout << "Free all client allocation" << endl;

        for (int i =0; i < id ; i++)
            close(all_client_ds[i]);

        close(ds_server_main);
        cout << "Ending server" << endl;

        if (shmdt(cloud_json) == -1
            || shmdt(n_threads_concurant) == -1
            || shmdt(n_threads_courant) == -1)
            cerr << "Can't detach shm" << endl;

        if (semctl(semid, 0, IPC_RMID) == -1) perror("Can't RM semaphore");
        exit(0);
    } else if (getppid() == parent_pid) {
        exit(0);
    }
    exit(1);
    return;
}
