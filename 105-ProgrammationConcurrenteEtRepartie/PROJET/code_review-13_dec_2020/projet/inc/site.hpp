#ifndef SITE_HPP
#define SITE_HPP

/* 
    struct Site:
        Un site à un nom (exemple "Montpellier")
        Un site propose un nombre de processeurs et un volume de stockage

 */

#define MAX_LEN_NAME_SITE 50
#define MAX_LEN_NAME_CLIENT 50
#define MAX_LEN_CPU 6


typedef struct {
    int id;
    char name[MAX_LEN_NAME_CLIENT];
    char ip_address[16];
    int port;
} Client;

typedef struct {
    int cpu;
    int memory;
} Resource;


typedef struct {
    char name[MAX_LEN_NAME_SITE];
    Resource ressource_available;
} Site;

/* 
    Création d'une structure Site
    @param name le nom du site
    @return un poiteur vers une nouvelle structure alloué dynamiquement ou NULL si echec
 */
Site* init_site(const char* name, int cpu, int memory);

/* 
    Affiche les ressources dispo
 */
void print_site(Site*);

/* 
    Désalloue l'espace alloué par site (utilisation d'un free())
 */
int destroy_site(Site*);

int get_cpu_available(Site*);

int get_memory_available(Site*);

int alloc_resource(Site* site, int, int );
int free_resource(Site* site, int, int);

#endif
