#ifndef RESERVATION_HPP
#define RESERVATION_HPP

#include "../inc/site.hpp"
#include "../inc/cloud.hpp"

typedef struct {
    Resource* resources;
    char** name; // of size [MAX_LEN_NAME_SITE];
    int number_server;
    Client client;
} Reservation;


Reservation* init_reservation(Cloud*, Client);

int destroy_reservation(Reservation*);

/* 
    @return the position of name_server in resources list
 */
Resource* get_resource_by_server_name(Reservation*, char* name_server);


Resource* get_resource_by_id(Reservation*, int id);

void print_reservation(Reservation*,char*);

/* 
    Ajout d'une ressource au tableau de ressources donnée en param
    @return NULL en cas d'échec ou le pointeur vers la nouvelle adresse
 */
Reservation* save_reservation(Reservation*, Resource, char*);

/* 
    Supprime l'élement j
 */
Resource free_reservation(Reservation*, char*);


#endif