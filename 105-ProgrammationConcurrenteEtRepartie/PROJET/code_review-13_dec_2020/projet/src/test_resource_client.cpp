#include <iostream>
#include <cstdlib>
#include <sys/types.h>
#include <unistd.h>
#include <cstring>
#include "../inc/site.hpp"
#include "../inc/cloud.hpp"


using namespace std;

int main(int argc, char const *argv[]) {
    cout << "Testing Resource Client" << endl;

    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " config_file_path" << endl;
        return 1;
    }

    srand(getpid() * time(NULL));
    Cloud* cloud = init_cloud_json(argv[1]);
    Client client;
    client.id = 1; client.port=34000;
    strcpy(client.name, "Massy");
    strcpy(client.ip_address, "127.0.0.1");


    int i = 0, cpu = 0, memory = 0;
    Site* site;

    if (cloud == NULL) {
        cerr << "Can't create cloud" << endl;
        return 1;
    }
    cout << "Cloud of " << cloud->size << " sites created." << endl;
    print_cloud(cloud);

    for (int j = 0; j < 2; j++) {
        i = rand() % cloud->size;
        site = get_site(cloud, i);
        cpu = rand() % get_cpu_available(site);
        memory = rand() % get_memory_available(site);

        cout << "Reservation du site '" << site->name << "' de " << cpu << "cpu et "<< memory << "Go" << endl;

        if (alloc_resource(client, site, cpu, memory) == -1) {
            cerr << "Impossible d'allouer les ressources" << endl;
        } else {
            cout << "allocation réussi" << endl;
        }
        cout << "MAJ dans 2sec"<< endl;
        sleep(2);
        print_cloud(cloud);
    }

    cout << "Libération de toutes les ressources" << endl;
    for (int j = 0; j < cloud->size; j++) {
        cout << "Site "<< cloud->sites[j]->name << " : " << endl;
        if (free_client_resource(client, cloud->sites[j]) == -1) 
            cerr << "\tImpossible de liberer la ressource" << endl;
        else
            cout << "\tLibération OK" << endl;        
    }

    if (destroy_cloud(cloud) == -1) {
        cerr << "Error destroy cloud" << endl;
        return -1;
    } else {
        cout << "Destroy OK" << endl;
    }
    return 0;
}
