#include <iostream>
#include <cstdlib>
#include <sys/types.h>
#include <cstring>
#include "../inc/site.hpp"
#include "../inc/cloud.hpp"
#include "../inc/reservation.hpp"


using namespace std;

int main(int argc, char const *argv[]) {
    cout << "Testing Resource Client" << endl;

    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " config_file_path" << endl;
        return 1;
    }

    Cloud* cloud = init_cloud_json(argv[1]);

    if (cloud == NULL) {
        cerr << "Can't create cloud" << endl;
        return 1;
    }
    cout << "Cloud of " << cloud->size << " sites created." << endl;
    print_cloud(cloud);

    Client client;
    client.id = 1; client.port=34000;
    strcpy(client.name, "Massy");
    strcpy(client.ip_address, "127.0.0.1");
    Reservation* res = init_reservation(cloud, client);

    print_reservation(res);

    if (alloc_resource(res, {.cpu= 20,.memory=5}, 1) != NULL) {
        print_reservation(res);
        if (free_resource(res, 1) != NULL) {
            cout << "Free resource ok" << endl;
            print_reservation(res);
        } else 
            cerr << "Error free_resource" << endl;
    } else
        cerr << "Error allocation" << endl;

    if (free_reservation(res) == -1) {
        cerr << "Error free_reservation" << endl;
        return 1;
    } else
        cout << "Free OK" << endl;
    return 0;
}
