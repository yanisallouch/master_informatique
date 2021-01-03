#include "../inc/define.hpp"
#include "../inc/cloud.hpp"
#include "../inc/reservation.hpp"
#include <string.h>
#include <cstdlib>
#include <cstdio>
#include <iostream>

using namespace std;

int reserve_resources(Cloud *cloud, Reservation *reservation, char *server_name, Resource resource)
{
    if (cloud == NULL ||
        reservation == NULL ||
        server_name == NULL ||
        resource.cpu < 0 ||
        resource.memory < 0)
        return -1;

    if (alloc_resource(get_site_by_name(cloud, server_name), resource.cpu, resource.memory) == -1)
    {
        return -1;
    }

    if (save_reservation(reservation, resource, server_name) == NULL)
    {
        cerr << "Impossible de trouver le server" << endl;
    }

    return 0;
}

int free_allocation(Cloud *cloud, Reservation *reservation, char *server_name)
{
    if (cloud == NULL ||
        reservation == NULL ||
        server_name == NULL)
        return -1;

    Resource r;
    if ((r = free_reservation(reservation, server_name)).cpu == -1)
        return -1;
    return free_resource(get_site_by_name(cloud, server_name), r.cpu, r.memory);
}

int free_all_allocation(Cloud *cloud, Reservation *reservation)
{
    if (cloud == NULL || reservation == NULL)
        return -1;

    Resource r;
    for (int i = 0; i < reservation->number_server; i++)
    {
        r = free_reservation(reservation, reservation->name[i]);
        free_resource(get_site_by_name(cloud, reservation->name[i]), r.cpu, r.memory);
    }
    return 0;
}

//-------- Ecoute et traitement de demande

commande interpret_cmd(char *cmd_str)
{
    commande cmd = {.cmd_type = CMD_ERR, .cpu = NULL, .memory = NULL, .server_name = NULL, .nb_server = 0};
    char *ptr = strtok(cmd_str, " ");

    if (strcmp(ptr, "help") == 0)
    {
        cmd.cmd_type = CMD_HELP;
        while (strtok(NULL, " ") != NULL)
            ; // ignorer le reset de la chaine
        return cmd;
    }
    else if (strcmp(ptr, "exit") == 0)
    {
        cmd.cmd_type = CMD_EXIT;
        while (strtok(NULL, " ") != NULL)
            ; // ignorer le reset de la chaine
        return cmd;
    }
    else if (strcmp(ptr, "alloc") == 0)
    {
        // allocation
        char server_name[MAX_LEN_NAME_SITE];
        int cpu, mem;
        ptr = strtok(NULL, " ");
        if (ptr == NULL)
        {
            cmd.cmd_type = CMD_ERR;
            return cmd;
        }
        strcpy(server_name, ptr);
        cpu = -1;
        if (sscanf(ptr, "%d", &cpu) == 1)
        {
            // syntax alloc cpu memory
            cmd.cmd_type = CMD_ALLOC_ALL;
            cmd.cpu = (int *)malloc(sizeof(int));
            (*cmd.cpu) = cpu;
            ptr = strtok(NULL, " ");
            if (ptr == NULL)
            {
                cmd.cmd_type = CMD_ERR;
                return cmd;
            }
            mem = atoi(ptr);
            if (mem < 0 || cpu < 0)
            {
                cmd.cmd_type = CMD_ERR;
                return cmd;
            }
            cmd.memory = (int *)malloc(sizeof(int));
            (*cmd.memory) = mem;
            cmd.nb_server = 1;
            return cmd;
        }
        else
        {
            // syntax alloc servername cpu memory ...
            cmd.server_name = (char **)malloc(sizeof(char *));
            cmd.server_name[0] = (char *)malloc(sizeof(char) * MAX_LEN_NAME_SITE);
            strcpy(cmd.server_name[0], server_name);
            cmd.nb_server = 1;
            cmd.cmd_type = CMD_ALLOC_WITH_NAMES;

            while (ptr != NULL)
            {
                ptr = strtok(NULL, " ");
                if (ptr == NULL)
                {
                    cmd.cmd_type = CMD_ERR;
                    return cmd;
                }
                cpu = atoi(ptr);
                ptr = strtok(NULL, " ");
                if (ptr == NULL)
                {
                    cmd.cmd_type = CMD_ERR;
                    return cmd;
                }
                mem = atoi(ptr);
                if (mem < 0 || cpu < 0)
                {
                    cmd.cmd_type = CMD_ERR;
                    return cmd;
                }
                cmd.cpu = (int *)realloc(cmd.cpu, cmd.nb_server * sizeof(int));
                cmd.cpu[cmd.nb_server - 1] = cpu;
                cmd.memory = (int *)realloc(cmd.memory, cmd.nb_server * sizeof(int));
                cmd.memory[cmd.nb_server - 1] = mem;
                ptr = strtok(NULL, " ");
                if (ptr == NULL)
                {
                    return cmd;
                }
                else
                {
                    cmd.nb_server++;
                    cmd.server_name = (char **)realloc(cmd.server_name, cmd.nb_server * sizeof(char *));
                    cmd.server_name[cmd.nb_server - 1] = (char *)malloc(sizeof(char) * MAX_LEN_NAME_SITE);
                    strcpy(cmd.server_name[cmd.nb_server - 1], ptr);
                }
            }
            cmd.cmd_type = CMD_ERR;
            return cmd;
        }
    }
    else if (strcmp(ptr, "free") == 0)
    {
        ptr = strtok(NULL, " ");
        if (ptr == NULL)
        {
            cmd.cmd_type = CMD_FREE_ALL;
            return cmd;
        }
        cmd.nb_server = 0;
        while (ptr != NULL)
        {
            cmd.nb_server++;
            cmd.server_name = (char **)realloc(cmd.server_name, cmd.nb_server * sizeof(char *));
            cmd.server_name[cmd.nb_server - 1] = (char *)malloc(sizeof(char) * MAX_LEN_NAME_SITE);
            strcpy(cmd.server_name[cmd.nb_server - 1], ptr);
            ptr = strtok(NULL, " ");
        }
        cmd.cmd_type = CMD_FREE_WITH_NAMES;
        return cmd;
    }
    else
    {
        cmd.cmd_type = CMD_ERR;
        return cmd;
    }
}

int execute_cmd_client(commande *cmd)
{

    if (cmd->cmd_type == CMD_ALLOC_ALL || cmd->cmd_type == CMD_EXIT || cmd->cmd_type == CMD_ALLOC_WITH_NAMES || cmd->cmd_type == CMD_ALLOC_WITH_NAMES || cmd->cmd_type == CMD_FREE_ALL || cmd->cmd_type == CMD_FREE_WITH_NAMES) {return 0;}
    else if (cmd->cmd_type == CMD_HELP)
    {
        cout << "alloc cpu mem:                   Allouer'cpu' cpu et 'mem' memory sur n'importe quel serveur" << endl;
        cout << "alloc (server_name cpu mem)+...: Allouer 'cpu' cpu et 'mem' memory sur le serveur 'server_name'" << endl;
        cout << "free (server_name)*:             Libérer tous ce qui a été alloué sur les serveurs spécifiés" << endl;
        cout << "free:                            Libérer toutes les ressources reserver." << endl;
        cout << "exit:                            Quitter proprement" << endl;
        return 0;
    }
    else
    {
        return -1;
    }
}
