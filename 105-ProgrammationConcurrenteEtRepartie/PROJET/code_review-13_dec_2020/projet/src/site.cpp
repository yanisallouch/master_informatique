#include <iostream>
#include "../inc/site.hpp"
#include <string.h>

using namespace std;

// --- Site

Site* init_site(const char* name, int cpu, int memory) {
    if (strlen(name) > MAX_LEN_NAME_SITE) {
        fprintf(stderr, "Nom trop long\n");
        return NULL;
    }
    Site* site = (Site*) malloc(sizeof(Site));

    strcpy(site->name, name);
    site->ressource_available = {.cpu = cpu, .memory = memory};

    return site;
}

void print_site(Site* site) {
    cout << "{'" << site->name << "': cpu: " << site->ressource_available.cpu << ", mem: " << site->ressource_available.memory <<"}";
    return;
}

int destroy_site(Site* site) {
    if (site != NULL) {
        free(site);
        return 0;
    } else {
        return -1;
    }
}

int get_cpu_available(Site* site) {
    if (site == NULL)
        return -1;
    else
        return site->ressource_available.cpu;
}

int get_memory_available(Site* site) {
    if (site == NULL)
        return -1;
    else
        return site->ressource_available.memory;
}

int alloc_resource(Site* site, int cpu, int memory) {
    if (site != NULL && get_cpu_available(site) >= cpu && get_memory_available(site) >= memory) {
        site->ressource_available.cpu -= cpu;
        site->ressource_available.memory -= memory;
        return 0;
    } else
        return -1;
}

int free_resource(Site* site, int cpu, int memory) {
    if (cpu < 0 || memory < 0) return -1;
    site->ressource_available.cpu += cpu;
    site->ressource_available.memory += memory;
    return 0;
}
