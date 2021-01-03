#include <stdio.h>
#include <cstdlib>
#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h> // htons
#include "../inc/protocol.hpp"

using namespace std;

int init_socket(const char *port)
{

    int ds = socket(PF_INET, SOCK_STREAM, 0);
    if (ds == -1)
    {
        perror("Error socket:");
        exit(1);
    }
    int enable = 1;
    if (setsockopt(ds, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(int)) < 0)
        perror("setsockopt(SO_REUSEADDR) failed");
    struct sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons((short)atoi(port));
    if (bind(ds, (struct sockaddr *)&addr, sizeof(addr)) == -1)
    {
        perror("Error bind:");
        exit(EXIT_FAILURE);
    }
    return ds;
}

int init_socket_client(const char* ip_address, const char* port){
    int ds = socket(PF_INET, SOCK_STREAM, 0);
    if (ds == -1)
    {
        perror("Error socket:"),
        exit(1);
    }

    struct sockaddr_in addr;
    addr.sin_family = AF_INET;
    if (inet_pton(AF_INET, ip_address, &addr.sin_addr.s_addr) == -1)
    {
        perror("Error converting ip_address:");
        exit(EXIT_FAILURE);
    }
    addr.sin_port = htons((short)atoi(port));

    if (connect(ds, (struct sockaddr *)&addr, sizeof(addr)) == -1)
    {
        perror("Error connecting server:");
        exit(EXIT_FAILURE);
    }
    return ds;
}

/*
    @return ds_client
*/
int waiting_for_client(int ds_server, int nb_client, struct sockaddr *addr_client, socklen_t *socklen)
{
    if (listen(ds_server, nb_client) == -1)
    {
        perror("Error listen:");
        exit(EXIT_FAILURE);
    }
    int ds_client = accept(ds_server, addr_client, socklen);

    if (ds_client == -1)
    {
        perror("Erreur accept:");
        exit(EXIT_FAILURE);
    }

    return ds_client;
}

int sendTCP(int socket, const char *buffer, int length)
{
    int sent = 0;
    int total = 0;
    if (send(socket, &length, sizeof(int), 0) <= 0) {return-1;}
    while (total < (int)length)
    {
        sent = send(socket, buffer + total, length - total, 0);
        if (sent <= 0)
            return sent;
        total += sent;
    }
    return total;
}

int recvTCP(int socket, char *buffer, int length)
{
    int received = 0, total = 0;
    if (recv(socket, &length, sizeof(int),0) < 0) {return-1;}
    while(total < (int)length)
    {
        received = recv(socket, buffer + total, length - total, 0);
        if (received <= 0)
            return received;
        total += received;
    }
    return total;
}

int sendCommandeTCP(int socket, const commande *cmd) {
    if (sendTCP(socket,(const char*) &cmd->cmd_type, sizeof(cmd_t)) <= 0){cerr<<"Error send"<<endl;return -1;}
    if (cmd->cmd_type == CMD_ALLOC_ALL || cmd->cmd_type == CMD_FREE_ALL || cmd->cmd_type == CMD_EXIT || cmd->cmd_type == CMD_ERR) {
        return 0;
    }
    if (sendTCP(socket,(const char*) &cmd->nb_server, sizeof(int)) <= 0){cerr<<"Error send"<<endl;return -1;}
    for (int i = 0; i < cmd->nb_server; i++) {
        if (cmd->cmd_type != CMD_FREE_WITH_NAMES) {
            if (sendTCP(socket,(const char*) &cmd->cpu[i], sizeof(int)) <= 0){cerr<<"Error send"<<endl;return -1;}
            if (sendTCP(socket,(const char*) &cmd->memory[i], sizeof(int)) <= 0){cerr<<"Error send"<<endl;return -1;}
        }
        if (sendTCP(socket,(const char*) cmd->server_name[i], MAX_LEN_NAME_SITE) <= 0){cerr<<"Error send"<<endl;return -1;}
    }
    return 0;
}
int recvCommandeTCP(int socket, commande *cmd) {
    if (recvTCP(socket,(char*) &cmd->cmd_type, sizeof(cmd_t)) <= 0){cerr<<"Error recv"<<endl;return -1;}
    if (cmd->cmd_type == CMD_ALLOC_ALL || cmd->cmd_type == CMD_FREE_ALL || cmd->cmd_type == CMD_EXIT || cmd->cmd_type == CMD_ERR) {
        return 0;
    }
    if (recvTCP(socket,(char*) &cmd->nb_server, sizeof(int)) <= 0){cerr<<"Error recv"<<endl;return -1;}
    for (int i = 0; i < cmd->nb_server; i++) {
        if (cmd->cmd_type != CMD_FREE_WITH_NAMES) {
            if (recvTCP(socket,(char*) &cmd->cpu[i], sizeof(int)) <= 0){cerr<<"Error recv"<<endl;return -1;}
            if (recvTCP(socket,(char*) &cmd->memory[i], sizeof(int)) <= 0){cerr<<"Error recv"<<endl;return -1;}
        }
        if (recvTCP(socket,(char*) cmd->server_name[i], sizeof(MAX_LEN_NAME_SITE)) <= 0){cerr<<"Error recv"<<endl;return -1;}
    }
    return 0;
}
