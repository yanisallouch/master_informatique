#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <pthread.h>

// Aucune modification n'est à faie dans ce fichier.

// vous devez trouver le sens de chaque champs de la structure. La strcuture est complète. 
struct sJetonsUneEtape{
  int max; 
  int nbJetonPris; 
  pthread_mutex_t mut; 
  pthread_cond_t cond;			     
};

typedef struct sJetonsUneEtape JetonsEtape;


// initialisation des champs de la structure *v. k est le nombre
// maximum de jetons rattachés à cette structure.  Cette fonction
// renvoie 0 en cas de succès sinon -1. Elle est appelée par le thread
// principal avant la création des threads joueurs.
int jetonsEtape_init(JetonsEtape * v, int k);

// s'il reste des jetons rattachés à *v, la fonction donne un jeton à
// l'appelant, sinon attend la libération de jeton. Cette fonction
// renvoie 0 en cas de succès sinon -1. Elle est appelée par un thread
// joueur avant de commencer chaque étape du jeu.
int demande_jeton(JetonsEtape * v);

// libère un jeton de la structure *v et réveil un des threads en
// attente d'un jeton rattachés à *v. Cette fonction renvoie 0 en cas
// de succès sinon -1. Elle est appelée par un thread joueur à la fin
// de chaque étape du jeu.
int liberer_jeton(JetonsEtape * v);

// libère la mémoire et les ressources de la structure *v. Cette
// fonction renvoie 0 en cas de succès sinon -1.  Elle est appelée par
// la main à la fin de l'exécution de tous les threads joueurs.
int jetonsEtape_destroy(JetonsEtape * v);

