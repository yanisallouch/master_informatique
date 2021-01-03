#include "../inc/include_server.hpp"
/*
    Ce programme initialise l'état des ressources et se met en écoute des demandes des clients
    Chaque client est gérer par un processus fils (fork()) qui éxecute la fonction .....();
 */
int main(int argc, char const *argv[])
{
    if (argc != 3)
    {
        cout << argv[0] << " chemin_vers_fichier_json port" << endl;
        exit(1);
    }
    const char *json_file = argv[1], *port = argv[2];

    //initialistation de la shared memory
    if ((shmid = shmget(IPC_PRIVATE, sizeof(char) * MAX_LEN_BUFFER_JSON, IPC_CREAT | 0600)) == -1)
    {
        perror("erreur shmget");
        exit(1);
    }
    if ((shm_concurant = shmget(IPC_PRIVATE, sizeof(int), IPC_CREAT | 0600)) == -1) {
        perror("erreur shmget");
        exit(1);
    }
    if ((shm_courant = shmget(IPC_PRIVATE, sizeof(int), IPC_CREAT | 0600)) == -1) {
        perror("erreur shmget");
        exit(1);
    }


    cloud = init_cloud_json(json_file);
    cloud_initial = init_cloud_json(json_file);
    if (cloud == NULL)
    {
        cerr << "Impossible de créer le Cloud" << endl;
        return 1;
    }

    cloud_json = (char *)shmat(shmid, NULL, 0);
    n_threads_concurant = (int*)shmat(shm_concurant, NULL, 0);
    n_threads_courant = (int*)shmat(shm_courant, NULL, 0);

    if ((cloud_json == (void *)-1) || (n_threads_concurant == (void *)-1) || (n_threads_courant == (void *)-1)) {
        perror("erreur shmat");
        exit(1);
    }

    int size_str = code_cloud(cloud, cloud_json, MAX_LEN_BUFFER_JSON);
    if (size_str == -1)
    {
        cerr << "Impossible de coder le cloud" << endl;
        return 1;
    }

    //shmdt((void *)cloud_json);
    // init semaphore;
    if ((semid = semget(IPC_PRIVATE, 4, IPC_CREAT | 0666)) == -1) {
        perror("erreur semget");
        exit(1);
    }
    semun ctrl;
    ctrl.array = (unsigned short *)malloc(4 * sizeof(unsigned short));
    ctrl.array[0] = 1;
    ctrl.array[1] = 1;
    ctrl.array[2] = 1;
    ctrl.array[3] = 1;

    if (semctl(semid, 0, SETALL, ctrl) == -1)
    {
        perror("erreur init sem");
    }
    id = 0;

    pthread_t thread_main;

    (*n_threads_concurant) = 1;
    (*n_threads_courant) = 0;

    if (pthread_create(&thread_main, NULL, wait_thread, NULL) != 0) {
        cerr << "Can't create the waiting thread" << endl;
        return 1;
    }
    cout << "****** Initialisation terminée ******" << endl;
    ds_server_main = init_socket(port);
    parent_pid = getpid();

    if (signal(SIGINT, server_signal_handler) == SIG_ERR) cerr << "Can't handel SIGINT" << endl;

    cout << "En attente des clients..." << endl;


    while (1) {
        struct sockaddr_in addr_client;
        socklen_t socklen;
        // en attente d'une demande de connection
        int ds_client = waiting_for_client(ds_server_main, 10, (struct sockaddr *)&addr_client, &socklen);
        int fork_return = -1;
        id++;

        all_client_ds = (int*) realloc(all_client_ds, id * sizeof(int));
        all_client_ds[id - 1] = ds_client;

        if ((fork_return = fork()) == -1)
        {
            cerr << "Can't fork" << endl;
            exit(EXIT_FAILURE);
        }
        else if (fork_return == 0)
        {
            // new process
            cout << "fils traitant le client" << endl;
            struct recv_msg r;
            r.ds_client = ds_client;
            n_threads_concurant = (int*)shmat(shm_concurant, NULL, 0);
            n_threads_courant = (int*)shmat(shm_courant, NULL, 0);
            if ((n_threads_concurant == (void *)-1) || (n_threads_courant == (void *)-1)) {
                perror("erreur shmat");
                exit(1);
            }
            semop(semid, &rendezvousP, 1);
            (*n_threads_concurant) += 1;
            semop(semid, &rendezvousV, 1);
            pthread_t thread_id;
            if (pthread_create(&thread_id, NULL, wait_thread, (void *)&r) != 0)
            {
                cerr << "Can't create the waiting thread" << endl;
                return 1;
            }
            if (manage_user(ds_client) != 0)
            {
                cerr<< "erreur manage_user"<<endl;
            }
            semop(semid, &verrouP, 1);
            if (pthread_cancel(thread_id) != 0)
                cerr << "Impossible de terminer le thread" << endl;

            if (pthread_join(thread_id, NULL) != 0)
                cerr << "impossible de joiner" << endl;
            close(ds_client);
            semop(semid, &rendezvousP, 1);
            (*n_threads_concurant) -= 1;
            semop(semid, &rendezvousV, 1);
            semop(semid, &verrouV, 1);

        }
        else
        {
            cout << "Traitement du client" << endl;
        }
    }


    return 0;
}
