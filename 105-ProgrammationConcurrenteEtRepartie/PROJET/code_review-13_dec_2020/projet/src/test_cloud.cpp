#include <iostream>
#include "../inc/cloud.hpp"

using namespace std;

/*
    Ce programme se connecte au serveur puis
        - le thread principal attend une demande saisi au clavier par l'utilisateur et l'envoi au serveur.
        - Un thread secondaire qui reste en écoute d'eventuels changement d'états des ressources.
 */
int main(int argc, char const *argv[])
{
    cout << "Test Cloud" << endl;

    if (argc != 2) {
        cerr << "Usage: %s conf.json" << endl;
        return 1;
    }

    Cloud* cloud = init_cloud_json(argv[1]);

    if (cloud == NULL)
        return 1;

    print_cloud(cloud);
    return 0;
}
