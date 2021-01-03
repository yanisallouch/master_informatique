#ifndef DEFINE_CLIENT_DEPANDANCE_HPP
#define DEFINE_CLIENT_DEPANDANCE_HPP

#include "site.hpp"
#include "cloud.hpp"
#include "reservation.hpp"

union semun {
    int val;               /* cmd = SETVAL */
    struct semidds *buf;   /* cmd = IPCSTAT ou IPCSET */
    unsigned short *array; /* cmd = GETALL ou SETALL */
    struct seminfo *__buf; /* cmd = IPCINFO (sous Linux) */
};

/* 
    reserve Resource from Cloud with name 'server_name' and add it to Reservation
    @return -1 error
 */
int reserve_resources(Cloud*, Reservation* ,char*, Resource);

int free_allocation(Cloud*, Reservation*, char*);
int free_all_allocation(Cloud*, Reservation*);   

commande interpret_cmd(char* cmd);
/* 
    @return -1 if error
 */
int execute_cmd_client(commande*);

#endif
