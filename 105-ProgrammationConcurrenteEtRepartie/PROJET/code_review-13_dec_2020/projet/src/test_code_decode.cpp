#include <iostream>
#include <cstdlib>
#include <sys/types.h>
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

    Cloud* cloud = init_cloud_json(argv[1]);

    if (cloud == NULL) {
        cerr << "Can't create cloud" << endl;
        return 1;
    }
    cout << "Cloud of " << cloud->size << " sites created." << endl;
    print_cloud(cloud);

    char code[MAX_LEN_BUFFER_JSON];
    if (code_cloud(cloud, code, MAX_LEN_BUFFER_JSON) >= 0) {
        cout << "Code réussi : " << endl << code << endl; 
        if ((cloud = decode_cloud(code, strlen(code))) == NULL) {
            cerr << "Err decode()" << endl;
            return 1;
        } else {
            cout << endl << "Decode réussi : " << endl;
            print_cloud(cloud);
        }
    } else {
        cerr << "Err code()" << endl;
        return 1;
    }

    return 0;
}
