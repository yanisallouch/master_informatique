#include "../inc/include_client.hpp"
/* 
    Ce programme se connecte au serveur puis 
        - le thread principal attend une demande saisi au clavier par l'utilisateur et l'envoi au serveur.
        - Un thread secondaire qui reste en écoute d'eventuels changement d'états des ressources.
 */
int main(int argc, char const *argv[])
{
    if (argc != 3)
    {
        fprintf(stderr, "Usage: %s ip_address port\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    const char *ip_address = argv[1], *port = argv[2];

    Client client;
    client.id = 1;
    client.port = atoi(port);
    strcpy(client.ip_address, ip_address);
    cout << "entrez votre nom svp : ";
    fgets(client.name, MAX_LEN_NAME_CLIENT, stdin);
    client.name[strlen(client.name) - 1] = '\0';
    cout << "vous êtes : " << client.name << " et votre id est : " << client.id << endl;

    ds_client = init_socket_client(ip_address, port);

    if ((semid = semget(IPC_PRIVATE, 1, IPC_CREAT | 0666)) == -1)
    {
        perror("erreur semget");
        exit(1);
    }

    semun ctrl;
    ctrl.val = 0;

    if (semctl(semid, 0, SETVAL, ctrl) == -1)
    {
        perror("erreur init sem");
    }

    fflush(stdin);
    pthread_t thread_id;
    struct recv_msg r;
    r.ds_client = ds_client;
    if (pthread_create(&thread_id, NULL, listen_modif, (void *)&r) != 0)
    {
        cerr << "Can't create the waiting thread" << endl;
        return 1;
    }
    while (1)
    {
        char in_buffer[500];
        in_buffer[499] = '\0';
        if (semctl(semid, 0, GETVAL) == 1) {
            semop(semid, &signalZ, 1);
            continue;
        }
        cout << "> ";
        fgets(in_buffer, 500, stdin);

        if (semctl(semid, 0, GETVAL) == 1) {
            cout << "Serveur en attente" << endl;
            semop(semid, &signalZ, 1);
            continue;
        }

        in_buffer[strlen(in_buffer) - 1] = '\0';
    
        commande cmd = interpret_cmd(in_buffer);
        
        if (execute_cmd_client(&cmd) == -1) {
            cerr << "Erreur à l'éxecution de la commande" << endl;
            continue;
        }
        
        if (cmd.cmd_type == CMD_HELP) continue;

        if (sendCommandeTCP(ds_client, &cmd) == -1) {perror("Can't send the message:");break;}

        if (cmd.cmd_type == CMD_EXIT) break;
        
        in_buffer[0] = '\0';
    }

    close(ds_client);
    
    if (pthread_cancel(thread_id) != 0)
        cerr << "Impossible de terminer le thread" << endl;

    if (pthread_join(thread_id, NULL) != 0)
        cerr << "impossible de joindre" << endl;
    return 0;
}
