#ifndef CLOUD_HPP   
#define CLOUD_HPP

// temporaire
#define MAX_LEN_BUFFER_JSON 1024
#include "site.hpp"

//-------- Ecoute et traitement de demande

typedef short cmd_t;
#define CMD_ALLOC_WITH_NAMES 2
#define CMD_ALLOC_ALL 3

#define CMD_FREE_WITH_NAMES 5
#define CMD_FREE_ALL 6

#define CMD_EXIT 7
#define CMD_HELP 8
#define CMD_ERR 9

typedef struct {
    cmd_t cmd_type;
    int* cpu;
    int* memory;
    char** server_name;
    int nb_server;
} commande;


/* 
    struct Ressource:
        Une instance de ressources represente plusieurs sites géographiquement distribués

 */
typedef struct {
    Site** sites;
    int size;
} Cloud;

commande* init_commande(Cloud* cloud);

/* 
    Création d'une structure Cloud à partir d'un fichier json
    Attention: malloc utilisé
    @param file_name nom du fichier json
    @return l'adresse du cloud créer
 */
Cloud* init_cloud_json(const char* file_name);

Cloud* init_cloud(const char* json);

/* 
    Désaloue l'esapce alloué par une struct cloud
    @param cloud le cloud à detruire
    @return 0 si ok -1 si erreur
 */
int destroy_cloud(Cloud*);

/* 
    Ajoute un site au cloud
    @return 0 si OK -1 sinon
 */
int add_site(Cloud*, Site*);

/* 
    Retourne l'adresse du site à la position pos
    @param pos position du site
    @return l'adresse du site ou NULL en cas d'erreur

 */
Site* get_site(Cloud*, int pos);


Site* get_site_by_name(Cloud*, char*);

/* 
    Supprime le site à la position pos du cloud
    @param pos position du site
    @return 0 si OK -1 sinon
 */
int rm_site(Cloud*, int pos);

int check_commande(Cloud*, commande*, Cloud*);

/* 
    Affiche un cloud
 */
void print_cloud(Cloud*);

/* 
    pour envoyer ces données il faut trouver un moyen de serializer la struct avant de l'envoyer

    // methode code()
    // methode decode()
 */

/* 
    serialize le cloud en chaine de caractère
    @param cloud le cloud
    @param size_string la taille de la chaine 
    @param code l'adresse d'une chaine
    @return the size of the string si réussi -1 sinon
 */
int code_cloud(Cloud* cloud, char* code, int size_string);

/* 
    @return NULL if error
 */
Cloud* decode_cloud(char* code, int);


#endif