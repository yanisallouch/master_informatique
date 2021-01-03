#ifndef PROTOCOL_HPP
#define PROTOCOL_HPP

#include "cloud.hpp"

struct recv_msg
{
    int ds_client;
    char msg[MAX_LEN_BUFFER_JSON];
    int size_msg;
};

int init_socket(const char *port);
int init_socket_client(const char*,const char*);
int waiting_for_client(int ds, int nb_client, struct sockaddr *addr_client, socklen_t *socklen);

int sendTCP(int socket, const char *buffer, int length);
int recvTCP(int socket, char *buffer, int length);

int sendCommandeTCP(int socket, const commande *buffer);
int recvCommandeTCP(int socket, commande *buffer);




#endif