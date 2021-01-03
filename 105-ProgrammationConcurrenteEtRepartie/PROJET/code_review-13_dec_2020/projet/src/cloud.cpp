#include <iostream>
#include <stdio.h>
#include <string.h>
#include "../inc/json-c/json.h"
#include "../inc/site.hpp"
#include "../inc/cloud.hpp"
#include "../inc/define.hpp"

using namespace std;


commande* init_commande(Cloud* cloud) {
    commande* cmd = (commande*) malloc(sizeof(commande));

    cmd->nb_server = cloud->size;
    cmd->cpu = (int*) malloc(sizeof(int) * cmd->nb_server);
    cmd->memory = (int*) malloc(sizeof(int) * cmd->nb_server);
    cmd->server_name = (char**) malloc(sizeof(char*) * cmd->nb_server);


    for (int i = 0; i < cloud->size; i++) {
        cmd->cpu[i] = 0;
        cmd->memory[i] = 0;
        cmd->server_name[i] = (char*) malloc(MAX_LEN_NAME_SITE * sizeof(char));
        strcpy(cmd->server_name[i], cloud->sites[i]->name);
    }
    return cmd;
}

// Implémentation des fonctions de Cloud

Cloud* init_cloud_json(const char* file_name) {
    //! Implémentation temporaire ne marchera pas pour un fichier de plus de MAX_LEN_BUFFER_JSON octets
    // <https://www.google.com/search?channel=fs&client=ubuntu&q=use+json+with+c>
    //malloc
    FILE *file;
    char buffer[MAX_LEN_BUFFER_JSON];

	file = fopen(file_name, "r");
    if (file == NULL) {
        perror("Can't acces file:");
        return NULL;
    }

	fread(buffer, MAX_LEN_BUFFER_JSON, 1, file);
    if (!feof(file)) {
        cerr << "Attention le fichier n'a pas été entièrement lu!"<< endl;
    }
	fclose(file);
    
    return init_cloud(buffer);
}


Cloud* init_cloud(const char* json) {
    Cloud* cloud;
	struct json_object *parsed_json;
	struct json_object *name;
	struct json_object *cpu;
	struct json_object *memory;
	struct json_object *site;

	size_t n_sites;

	size_t i;

    parsed_json = json_tokener_parse(json);

    n_sites = json_object_array_length(parsed_json);
    cloud = (Cloud*) malloc(sizeof(Cloud));
    if (n_sites > 0) {
        cloud->sites = (Site**) malloc(n_sites * sizeof(Site*));
        if (cloud->sites == NULL) {
            perror("Erreur malloc:");
            return NULL;
        }
        cloud->size = n_sites;
    }
    for (i = 0; i < n_sites; i++) {
        site = json_object_array_get_idx(parsed_json, i);

        json_object_object_get_ex(site, "name", &name);
        json_object_object_get_ex(site, "cpu", &cpu);        
        json_object_object_get_ex(site, "memory", &memory);        

        cloud->sites[i] = init_site(
            json_object_get_string(name), 
            json_object_get_int(cpu), 
            json_object_get_int(memory)
        );
        
    }
    return cloud;
}


int destroy_cloud(Cloud* cloud) {
    for (int i = 0; i < cloud->size; i++) {
        if (destroy_site(cloud->sites[i]) == -1) return -1;
    }
    free(cloud->sites);
    cloud->size = 0;
    return 0;
}

int add_site(Cloud* cloud,Site* site) {
    if (cloud == NULL || site == NULL) {
        cerr << "Null pointeur" << endl;
        return -1;
    }
    cloud->sites = (Site**) realloc(cloud->sites, cloud->size + 1);
    if (cloud->sites == NULL) return -1;
    cloud->sites[cloud->size] = site;
    cloud->size += 1;
    return 0;
}

Site* get_site(Cloud* cloud, int i) {
    if (cloud == NULL || i >= cloud->size) return NULL;
    return cloud->sites[i];
}

Site* get_site_by_name(Cloud* cloud, char* server_name) {
    int i;
    for(i = 0 ; i < cloud->size; i++)
        if (strcmp(cloud->sites[i]->name, server_name) == 0)
            break;
    
    return (i<cloud->size?cloud->sites[i]:NULL);
}

int rm_site(Cloud* cloud, int i) {
    if (cloud == NULL || i >= cloud->size || cloud->sites[i] == NULL) return -1;
    if (destroy_site(cloud->sites[i]) == -1) return -1;
    for (int j = i; j < cloud->size - 1; j++)
        cloud->sites[j] = cloud->sites[j+1];
    cloud->sites = (Site**) realloc(cloud->sites, cloud->size - 1);
    cloud->size -= 1;
    return (cloud->sites==NULL)?-1:0;
}

int check_commande(Cloud* cloud, commande* cmd, Cloud* cloud_initial) {
    if(cloud == NULL || cloud->sites == NULL) return -2;
    for (int i = 0; i < cmd->nb_server; i++) {
        int cpu = cmd->cpu[i], mem = cmd->memory[i];
        char* server_name = cmd->server_name[i];
        Site* site = get_site_by_name(cloud, server_name);
        Site* site_init = get_site_by_name(cloud_initial, server_name);
        if (site == NULL) return -2;
        if (cpu > get_cpu_available(site_init) || mem > get_memory_available(site_init) ) return -2;
        if (get_cpu_available(site) < cpu || get_memory_available(site) < mem) return -1;
    }
    return 0;
}


void print_cloud(Cloud* cloud) {
    if (cloud == NULL) {
        cout << "cloud = NULL" << endl;
        return;
    }
    for(int i = 0; i < cloud->size; i++) {
        cout << "Site de '" << cloud->sites[i]->name<< "': ";
        cout << "{cpu: " << cloud->sites[i]->ressource_available.cpu << ",";
        cout << "memory: " << cloud->sites[i]->ressource_available.memory << "}";
        cout << endl;
    }
    return;
}

int code_cloud(Cloud* cloud, char* code, int size_string) {
    if (cloud == NULL || code == NULL) return -1;
    for (int i = 0; i < size_string; i++)
        code[i] = '\0';
    
    int curseur = 0;
    code[curseur++] = '[';
    for (int i = 0; i < cloud->size; i++) {
        char snum[MAX_LEN_CPU];
        code[curseur++] = '{';
        strcat(code, "'name':'");curseur+=strlen("'name':'");
        strcat(code, cloud->sites[i]->name);curseur+=strlen(cloud->sites[i]->name);
        strcat(code, "','cpu':");curseur+=strlen("','cpu':");
        sprintf(snum, "%d", get_cpu_available(cloud->sites[i]));
        strcat(code, snum);curseur+=strlen(snum);
        strcat(code, ",'memory':");curseur+=strlen(",'memory':");
        sprintf(snum, "%d", get_memory_available(cloud->sites[i]));
        strcat(code, snum);curseur+=strlen(snum);
        code[curseur++] = '}';
        code[curseur++] = ',';

    }
    code[--curseur] =']';
    return curseur;
}

Cloud* decode_cloud(char* code, int size_string) {
    if (size_string > MAX_LEN_BUFFER_JSON) return NULL;
    return init_cloud(code);
}